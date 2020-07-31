package com.peilian.dataplatform.repository;

import com.peilian.dataplatform.entity.DataConvert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhengshangchao
 */
public interface DataConvertRepository extends JpaRepository<DataConvert, Long> {

    /**
     * 根据apiCode查找bsh脚本的配置信息
     *
     * @param apiCode
     * @return
     */
    List<DataConvert> findByApiCode(String apiCode);
}
