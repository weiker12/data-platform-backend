package com.peilian.dataplatform.config;

import com.peilian.dataplatform.entity.DataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final static Map<String, MysqlConnector> cacheMap = new HashMap<>();


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
        LocalDateTime updateTime = dataSource.getUpdateTime();
        // 如果当前更新了数据库的连接信息配置则需更新缓存的map，目前根据当前时间来判断
        boolean updateFlag = updateTime.isAfter(LocalDate.now().atStartOfDay());
        if(Objects.isNull(mysqlConnector) || updateFlag) {
            mysqlConnector = new MysqlConnector(url, username, password);
            cacheMap.put(dsCode, mysqlConnector);
        }
        return mysqlConnector;
    }

}
