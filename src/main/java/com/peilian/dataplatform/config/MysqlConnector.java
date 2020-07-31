package com.peilian.dataplatform.config;

import com.peilian.dataplatform.entity.BeanProxy;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.util.Assert;

import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据mysql数据库连接信息获取数据库操作的jdbcTemplate
 *
 * @author zhengshangchao
 */
@Slf4j
public class MysqlConnector {

    private String url;

    private String username;

    private String password;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public MysqlConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        hikariDataSource.setMaximumPoolSize(2);
        hikariDataSource.setMinimumIdle(1);
        hikariDataSource.setAutoCommit(true);
        hikariDataSource.addDataSourceProperty("cachePrepStmts", "true");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(hikariDataSource);
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询结果集
     *
     * @param sql
     * @return
     */
    public List<BeanProxy> query(String sql) {
        // 获取查询结果集的返回字段名
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, new HashMap<>());
        SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
        int count = metaData.getColumnCount();
        final String[] columnNames = new String[count];
        for(int i = 0; i < count; i++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
        }
        // 返回查询结果集的记录
        List<BeanProxy> beanProxyList = jdbcTemplate.query(sql,  (rs, rowNum) -> {
            BeanProxy beanProxy = new BeanProxy();
            for(String columnName : columnNames) {
                beanProxy.setValue(columnName, rs.getObject(columnName));
            }
            return beanProxy;
        });
        return beanProxyList;
    }

}
