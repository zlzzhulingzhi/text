package cn.qbs.wa.union.admin.pojo.systemuser;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 平台系统子用户表(SystemUser)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:07
 */
@Data
public class SystemUserPageRequest extends BasePageRequest {


    @ApiModelProperty("禁用/启用")
    Integer enabled;

    @ApiModelProperty("搜索内容")
    String searchContent;

    @ApiModelProperty("加密搜索内容")
    String encodeSearchContent;

}

