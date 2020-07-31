package com.peilian.dataplatform.repository;

import com.peilian.dataplatform.entity.DataFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhengshangchao
 */
@Repository
public interface DataFlowRepository extends JpaRepository<DataFlow, Long> {

    /**
     * 根据apiCode查询配置信息
     *
     * @param apiCode
     * @return
     */
    List<DataFlow> findByApiCode(String apiCode);

}
