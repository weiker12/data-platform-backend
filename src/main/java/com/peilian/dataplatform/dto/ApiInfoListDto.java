package com.peilian.dataplatform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 数据源配置表(DataSource)实体类
 *
 * @author zhengshangchao
 * @since 2020-07-27 15:16:35
 */
@Data
public class ApiInfoListDto implements Pageable {

    /**
     * 接口代码
     */
    @ApiModelProperty(value = "接口代码", example = "test")
    private String apiCode;

    /**
     * 数据源名称
     */
    @ApiModelProperty(value = "数据源名称", example = "test")
    private String apiName;

    @Override
    public int getPageNumber() {
        return 1;
    }

    @Override
    public int getPageSize() {
        return 10;
    }

    @Override
    public long getOffset() {
        return 0;
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}