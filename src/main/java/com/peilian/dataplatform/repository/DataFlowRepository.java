package com.peilian.dataplatform.repository;

import com.peilian.dataplatform.entity.DataFlow;
import com.peilian.dataplatform.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhengshangchao
 */
@Repository
public interface DataFlowRepository extends JpaRepository<DataFlow, Long>, JpaSpecificationExecutor<DataSource> {

    /**
     * 根据apiCode查询配置信息
     *
     * @param apiCode
     * @return
     */
    List<DataFlow> findByApiCode(String apiCode);

    /**
     * 根据apiCode删除原有配置信息
     *
     * @param apiCode
     * @return
     */
    Integer deleteByApiCode(String apiCode);

}
