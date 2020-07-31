package com.peilian.dataplatform.config;

import com.peilian.dataplatform.entity.DataSource;
import com.peilian.dataplatform.repository.DataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * mysql工厂
 *
 * @author zhengshangchao
 */
@Component
public class MysqlFactory {

    @Autowired
    DataSourceRepository dataSourceRepository;

    /**
     * 创建连接mysql实例的工厂
     *
     * @param dataSource
     * @return
     */
    public MysqlConnector build(DataSource dataSource) {
        Assert.notNull(dataSource, "数据源配置不能为空！");
        String url = dataSource.getUrl();
        String username = dataSource.getUsername();
        String password = dataSource.getPassword();
        MysqlConnector mysqlConnector = new MysqlConnector(url, username, password);
        return mysqlConnector;
    }

}
