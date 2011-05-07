/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gengweibo.dao.WeiboDao;
import com.gengweibo.weibo.IWeibo;
import com.gengweibo.weibo.Weibo;
import com.gengweibo.weibo.WeiboManager;
import com.gengweibo.weibo.WeiboType;
import com.google.inject.Inject;
import com.google.inject.internal.Lists;

/**
 * @author auzll@msn.com
 * @since 2011-04-06
 */
public class WeiboDaoJdbcImpl implements WeiboDao {
    private static final Log LOG = LogFactory.getLog(WeiboDaoJdbcImpl.class);

    @Inject
    private DataSource dataSource;

    private void closeQuietly(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("method:closeQuietly", e);
                }
            }
        }
    }

    private void closeQuietly(Statement stmt) {
        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("method:closeQuietly", e);
                }
            }
        }
    }

    private void closeQuietly(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("method:closeQuietly", e);
                }
            }
        }
    }

    public void save(IWeibo weibo) {
        Connection conn = null;
        PreparedStatement pstmtQuery = null;
        ResultSet rs = null;
        PreparedStatement pstmtUpdate = null;
        PreparedStatement pstmtSave = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            Weibo wei = (Weibo) weibo;

            // 先查找，如果已存在就更新，否则直接新插入
            pstmtQuery = conn
                    .prepareStatement("select * from gwb_weibo where weibo_id=?");
            pstmtQuery.setString(1, wei.getWeiboId());
            rs = pstmtQuery.executeQuery();

            if (rs.next()) {

                // 更新
                pstmtUpdate = conn
                        .prepareStatement("update gwb_weibo set access_token=?,token_secret=?,account_id=? where weibo_id=?");
                pstmtUpdate.setString(1, wei.getAccessor().accessToken);
                pstmtUpdate.setString(2, wei.getAccessor().tokenSecret);
                pstmtUpdate.setString(3, wei.getAccountId());
                pstmtUpdate.setString(4, wei.getWeiboId());
                pstmtUpdate.executeUpdate();

                weibo.setSynUpdate(Boolean.valueOf(rs.getString("syn_update")));

            } else {
                // 新保存
                pstmtSave = conn
                        .prepareStatement("insert into gwb_weibo(account_id,access_token,token_secret,type,weibo_id,syn_update) values(?,?,?,?,?,?)");
                pstmtSave.setString(1, wei.getAccountId());
                pstmtSave.setString(2, wei.getAccessor().accessToken);
                pstmtSave.setString(3, wei.getAccessor().tokenSecret);
                pstmtSave.setString(4, wei.getType().getEnName());
                pstmtSave.setString(5, wei.getWeiboId());

                //
                wei.setSynUpdate(true);
                pstmtSave.setString(6, String.valueOf(wei.isSynUpdate()));

                pstmtSave.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (null != conn) {
                try {
                    conn.rollback();
                } catch (SQLException re) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("method:save", re);
                    }
                }
            }
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
            closeQuietly(pstmtQuery);
            closeQuietly(rs);
            closeQuietly(pstmtUpdate);
            closeQuietly(pstmtSave);
        }
    }

    public void delete(String weiboId) {
        Connection conn = null;
        PreparedStatement pstmtDel = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            // 删除
            pstmtDel = conn
                    .prepareStatement("delete from gwb_weibo where weibo_id=?");
            pstmtDel.setString(1, weiboId);
            pstmtDel.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            if (null != conn) {
                try {
                    conn.rollback();
                } catch (SQLException re) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("method:delete", re);
                    }
                }
            }

            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
            closeQuietly(pstmtDel);
        }
    }

    public List<IWeibo> queryRelatedList(String weiboId) {
        Connection conn = null;

        PreparedStatement pstmtQueryAccountId = null;
        ResultSet rsQueryAccountId = null;

        PreparedStatement pstmtQueryWeibo = null;
        ResultSet rsQueryWeibo = null;

        try {
            List<IWeibo> list = Collections.emptyList();

            conn = dataSource.getConnection();

            // 先尝试删除
            pstmtQueryAccountId = conn.prepareStatement("select account_id from gwb_weibo where weibo_id=?");
            pstmtQueryAccountId.setString(1, weiboId);
            rsQueryAccountId = pstmtQueryAccountId.executeQuery();
            if (rsQueryAccountId.next()) {
                String accountId = rsQueryAccountId.getString(1);
                pstmtQueryWeibo = conn
                        .prepareStatement("select * from gwb_weibo where account_id=?");
                pstmtQueryWeibo.setString(1, accountId);
                rsQueryWeibo = pstmtQueryWeibo.executeQuery();

                list = Lists.newArrayList();
                while (rsQueryWeibo.next()) {
                    String type = rsQueryWeibo.getString("type");
                    String accessToken = rsQueryWeibo.getString("access_token");
                    String tokenSecret = rsQueryWeibo.getString("token_secret");
                    boolean synUpdate = Boolean.valueOf(rsQueryWeibo.getString("syn_update"));

                    Weibo weibo = (Weibo) WeiboManager.newWeibo(WeiboType.of(type));
                    weibo.setAccountId(accountId);
                    weibo.getAccessor().requestToken = null;
                    weibo.getAccessor().accessToken = accessToken;
                    weibo.getAccessor().tokenSecret = tokenSecret;
                    weibo.setSynUpdate(synUpdate);
                    
                    try {
                        weibo.bindWeiboAccountContext();
                        list.add(weibo);
                    } catch (Exception e) {
                        LOG.error("method:queryRelatedList,desc:fail", e);
                    }
                    
                }
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
            closeQuietly(pstmtQueryAccountId);
            closeQuietly(rsQueryAccountId);
            closeQuietly(pstmtQueryWeibo);
            closeQuietly(rsQueryWeibo);
        }
    }

    public void updateSyn(String weiboId, boolean synUpdate) {
        Connection conn = null;
        PreparedStatement pstmtUpdate = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            // 更新
            pstmtUpdate = conn
                    .prepareStatement("update gwb_weibo set syn_update=? where weibo_id=?");
            pstmtUpdate.setString(1, String.valueOf(synUpdate));
            pstmtUpdate.setString(2, weiboId);
            pstmtUpdate.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            if (null != conn) {
                try {
                    conn.rollback();
                } catch (SQLException re) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("method:updateSyn", re);
                    }
                }
            }

            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
            closeQuietly(pstmtUpdate);
        }
    }

}
