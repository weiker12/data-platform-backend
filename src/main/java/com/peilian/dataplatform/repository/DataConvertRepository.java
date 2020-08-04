package com.peilian.dataplatform.repository;

import com.peilian.dataplatform.entity.DataConvert;
import com.peilian.dataplatform.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhengshangchao
 */
@Repository
public interface DataConvertRepository extends JpaRepository<DataConvert, Long>, JpaSpecificationExecutor<DataSource> {

    /**
     * 根据apiCode查找bsh脚本的配置信息
     *
     * @param apiCode
     * @return
     */
    List<DataConvert> findByApiCode(String apiCode);

    /**
     * 根据apiCode删除转换配置信息
     *
     * @param apiCode
     * @return
     */
    Integer deleteByApiCode(String apiCode);
}
