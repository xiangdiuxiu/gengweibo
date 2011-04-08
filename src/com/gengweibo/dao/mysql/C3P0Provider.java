/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.dao.mysql;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author auzll@msn.com
 */
public class C3P0Provider implements Provider<DataSource>{
    @Inject @Named("db.driverClass") private String driverClass;
    @Inject @Named("db.jdbcUrl") private String jdbcUrl;
    @Inject @Named("db.user") private String user;
    @Inject @Named("db.password") private String password;
    @Inject @Named("db.autoCommitOnClose") private boolean autoCommitOnClose;
    @Inject @Named("db.initialPoolSize") private int initialPoolSize;
    @Inject @Named("db.minPoolSize") private int minPoolSize;
    @Inject @Named("db.maxPoolSize") private int maxPoolSize;
    @Inject @Named("db.maxIdleTime") private int maxIdleTime;
    @Inject @Named("db.acquireIncrement") private int acquireIncrement;
    @Inject @Named("db.checkoutTimeout") private int checkoutTimeout;
    @Inject @Named("db.maxIdleTimeExcessConnections") private int maxIdleTimeExcessConnections;

    public DataSource get() {
        try {
            ComboPooledDataSource source = new ComboPooledDataSource();
            source.setDriverClass(driverClass);
            source.setJdbcUrl(jdbcUrl);
            source.setUser(user);
            source.setPassword(password);
            source.setAutoCommitOnClose(autoCommitOnClose);
            source.setInitialPoolSize(initialPoolSize);
            source.setMinPoolSize(minPoolSize);
            source.setMaxPoolSize(maxPoolSize);
            source.setMaxIdleTime(maxIdleTime);
            source.setAcquireIncrement(acquireIncrement);
            source.setCheckoutTimeout(checkoutTimeout);
            source.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
            return source;
        } catch (PropertyVetoException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
