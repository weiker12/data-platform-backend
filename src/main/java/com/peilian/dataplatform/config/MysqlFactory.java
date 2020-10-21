package com.peilian.dataplatform.config;

import com.peilian.dataplatform.entity.DataSource;
import com.peilian.dataplatform.repository.DataSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * mysql工厂
 *
 * @author zhengshangchao
 */
@Component
public class MysqlFactory {

    /**
     * 以dsCode为键的缓存MysqlConnector，避免频繁创建MysqlConnector产生开销
     */
    private final static Map<String, MysqlConnector> cacheMap= new HashMap<>();


    /**
     * 创建连接mysql实例的工厂
     *
     * @param dataSource
     * @return
     */
    public MysqlConnector build(DataSource dataSource) {
        Assert.notNull(dataSource, "数据源配置不能为空！");
        String dsCode = dataSource.getDsCode();
        String url = dataSource.getUrl();
        String username = dataSource.getUsername();
        String password = dataSource.getPassword();
        MysqlConnector mysqlConnector = cacheMap.get(dsCode);
        if(Objects.isNull(mysqlConnector)) {
            mysqlConnector = new MysqlConnector(url, username, password);
            cacheMap.put(dsCode, mysqlConnector);
        }
        return mysqlConnector;
    }

}
