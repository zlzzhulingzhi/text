package cn.qbs.wa.teach.organization.pojo.studentgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.organization.entity.StudentGroup;

/**
 * 学员-分组(StudentGroup)学员-分组详情
 *
 * @author makejava
 * @since 2022-03-28 16:07:15
 */
@Data
public class StudentGroupDetailResponse extends StudentGroup {

    @ApiModelProperty(value = "标签名")
    private String groupName;


    @ApiModelProperty(value = "学生标签id")
    private Long studentGroupId;

    @ApiModelProperty(value = "标识,0:选中,1:未选中")
    private Integer flag=1;
}

