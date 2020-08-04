package com.peilian.dataplatform.repository;

import com.peilian.dataplatform.entity.ApiSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author zhengshangchao
 */
@Repository
public interface ApiSourceRepository extends JpaRepository<ApiSource, Long>, JpaSpecificationExecutor<ApiSource> {

    /**
     * 根据apiCode查询配置信息
     *
     * @param apiCode
     * @return
     */
    ApiSource findByApiCode(String apiCode);

    /**
     * 根据apiCode删除配置信息
     *
     * @param apiCode
     * @return
     */
    Integer deleteByApiCode(String apiCode);

}
