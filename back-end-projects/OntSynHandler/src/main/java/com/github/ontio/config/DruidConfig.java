package com.github.ontio.config;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/12/6
 */


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Druid配置类
 * 1、可以监控数据库访问性能，内置了插件：StatFilter，能够详细统计SQL执行性能，用于线上分析数据库访问性能。
 * 2、替换DBCP、C3P0数据库连接池，提供了一个高效稳定的扩展性能好的数据库连接池DruidDataSource。
 * 3、数据库加密，DruidDriuiver和DruidDataSource都支持PasswordCallback。
 * 4、SQL执行日志。
 * 5、扩展JDBC，如果你要对JDBC层有编程的需求，可以通过Druid提供的Filter-Chain机制，很方便编写JDBC层的扩展插件
 * http://localhost:port/druid/login.html
 */
//@Profile(value={"default","dev","pro"})
@Configuration
@Slf4j
public class DruidConfig extends BaseDruidConfig {

    @Bean
    public ServletRegistrationBean druidServlet() throws Exception {
        return getServletRegistrationBean(username, publicKey, password, logSlowSql);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        return getFilterRegistrationBean();
    }

    /**
     * 这个应该是数据库连接池配置
     * @return
     * @throws Exception
     */
    @Bean
    public DataSource druidDataSource() throws Exception {
        return getDataSource(dbUrl, username, publicKey, password, driverClassName, initialSize, minIdle, maxActive, maxWait, timeBetweenEvictionRunsMillis, minEvictableIdleTimeMillis, validationQuery, testWhileIdle, testOnBorrow, testOnReturn, connectionProperties, filters, log);
    }

}
