package cn.qbs.wa.train.logistics.pojo.category;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *
 *
 * @author makejava
 * @since 2021-11-08 13:32:05
 */
@Data
public class CategoryListRequest extends BasePageRequest {

    @ApiModelProperty(value = "父主键")
    private Long parentId;



}

