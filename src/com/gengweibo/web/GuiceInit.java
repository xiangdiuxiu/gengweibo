/**
 * http://auzll.iteye.com/
 */
package com.gengweibo.web;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import com.gengweibo.dao.WeiboDao;
import com.gengweibo.dao.mysql.C3P0Provider;
import com.gengweibo.dao.mysql.WeiboDaoJdbcImpl;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

/**
 * 初始化Guice Injector
 * 
 * @author auzll@msn.com
 * @since 2011-04-07
 */
public class GuiceInit extends HttpServlet {
    private static final long serialVersionUID = 8466244375434657813L;

    public void init(ServletConfig config) throws ServletException {
        Injector injector = Guice.createInjector(new Module() {
            public void configure(Binder binder) {
                binder.bind(AccountAcction.class).in(Singleton.class);
                binder.bind(WeiboDao.class).to(WeiboDaoJdbcImpl.class)
                        .in(Singleton.class);
                binder.bind(DataSource.class).toProvider(C3P0Provider.class)
                        .in(Singleton.class);
                Properties properties = new Properties();
                try {
                    properties.load(Thread.currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream("database.properties"));
                } catch (IOException e) {
                    throw new ExceptionInInitializerError(e);
                }
                Names.bindProperties(binder, properties);
            }
        });

        config.getServletContext().setAttribute(Injector.class.getName(),
                injector);
    }
}
