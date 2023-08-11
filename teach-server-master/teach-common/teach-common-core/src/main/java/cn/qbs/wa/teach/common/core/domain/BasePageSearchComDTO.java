package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 19:52
 */
@Data
public class BasePageSearchComDTO {

    public static final int MAX_SIZE = 1000;

    public static final int MIDDLE_SIZE = 500;

    @ApiModelProperty(value = "页码大小")
    private Integer size;

    @ApiModelProperty(value = "当前页")
    private Integer current;

    @ApiModelProperty(value = "排序属性")
    private String sortField;

    @ApiModelProperty(value = "排序正序/倒序")
    private String sortOrder;



}
