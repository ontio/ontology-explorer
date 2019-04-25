package com.github.ontio.config;

/**
 * @author zhouq
 * @version 1.0
 * @date 2018/12/6
 */


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Druid配置类
 * 1、可以监控数据库访问性能，内置了插件：StatFilter，能够详细统计SQL执行性能，用于线上分析数据库访问性能。
 * 2、替换DBCP、C3P0数据库连接池，提供了一个高效稳定的扩展性能好的数据库连接池DruidDataSource。
 * 3、数据库加密，DruidDriuiver和DruidDataSource都支持PasswordCallback。
 * 4、SQL执行日志。
 * 5、扩展JDBC，如果你要对JDBC层有编程的需求，可以通过Druid提供的Filter-Chain机制，很方便编写JDBC层的扩展插件
 * http://localhost:port/druid/login.html
 */
@Configuration
@Slf4j
public class BaseDruidConfig {

    @Value("${spring.datasource.url}")
    protected String dbUrl;

    @Value("${spring.datasource.username}")
    protected String username;

    @Value("${spring.datasource.password}")
    protected String password;

    @Value("${spring.datasource.publicKey}")
    protected String publicKey;

    @Value("${spring.datasource.driverClassName}")
    protected String driverClassName;

    @Value("${spring.datasource.initialSize}")
    protected int initialSize;

    @Value("${spring.datasource.minIdle}")
    protected int minIdle;

    @Value("${spring.datasource.maxActive}")
    protected int maxActive;

    @Value("${spring.datasource.maxWait}")
    protected int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    protected int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    protected int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    protected String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    protected boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    protected boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    protected boolean testOnReturn;

    @Value("${spring.datasource.filters}")
    protected String filters;

    @Value("${spring.datasource.logSlowSql}")
    protected String logSlowSql;

    @Value("${spring.datasource.connectionProperties}")
    protected String connectionProperties;

    public DataSource getDataSource(String dbUrl, String username, String publicKey, String password, String driverClassName, int initialSize, int minIdle, int maxActive, int maxWait, int timeBetweenEvictionRunsMillis, int minEvictableIdleTimeMillis, String validationQuery, boolean testWhileIdle, boolean testOnBorrow, boolean testOnReturn, String connectionProperties, String filters, Logger log) throws Exception {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        //数据库密码加密的话使用这个
        //datasource.setPassword(ConfigTools.decrypt(publicKey, password));
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setConnectionProperties(connectionProperties);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            log.error("druid configuration initialization filter:{}", e);
        }
        return datasource;
    }

    public ServletRegistrationBean getServletRegistrationBean(String username, String publicKey, String password, String logSlowSql) throws Exception {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");//配置访问URL
        reg.addInitParameter("loginUsername", username);  //配置用户名，这里使用数据库账号。
        //数据库密码加密的话使用这个
//        reg.addInitParameter("loginPassword", ConfigTools.decrypt(publicKey, password));
        reg.addInitParameter("loginPassword", password);  //配置用户名，这里使用数据库密码
        reg.addInitParameter("logSlowSql", logSlowSql);   //是否启用慢sql
        return reg;
    }


    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");  //配置那些资源不被拦截
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }
}
