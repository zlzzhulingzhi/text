package cn.qbs.wa.train.logistics.pojo.groups;

import cn.qbs.wa.train.logistics.entity.Groups;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用分组表(Groups)分页查询通用分组表响应
 *
 * @author makejava
 * @since 2022-03-28 09:27:34
 */
@Data
public class GroupsPageResponse extends Groups {
    @ApiModelProperty(value = "主键ID")
    private Long groupId;
}

