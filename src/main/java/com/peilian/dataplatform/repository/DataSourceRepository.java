package com.peilian.dataplatform.repository;

import com.peilian.dataplatform.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author zhengshangchao
 */
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long>, JpaSpecificationExecutor<DataSource> {

    /**
     * 根据ds_code查询配置信息
     *
     * @param dsCode
     * @return
     */
    DataSource findByDsCode(String dsCode);

}
