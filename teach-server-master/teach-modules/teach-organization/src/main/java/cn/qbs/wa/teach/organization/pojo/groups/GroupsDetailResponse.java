package cn.qbs.wa.teach.organization.pojo.groups;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.organization.entity.Groups;

/**
 * 通用分组表(Groups)通用分组表详情
 *
 * @author makejava
 * @since 2022-03-28 09:27:34
 */
@Data
public class GroupsDetailResponse extends Groups {
    @ApiModelProperty(value = "标签ID")
    private Long groupId;
}

