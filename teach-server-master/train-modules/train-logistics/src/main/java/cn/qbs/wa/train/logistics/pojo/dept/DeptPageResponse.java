package cn.qbs.wa.train.logistics.pojo.dept;

import cn.qbs.wa.train.logistics.entity.Dept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门表(Dept)分页查询部门表响应
 *
 * @author makejava
 * @since 2021-11-17 09:29:20
 */
@Data
public class DeptPageResponse extends Dept {
    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;
}

