package cn.qbs.wa.teach.organization.pojo.studentgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.organization.entity.StudentGroup;

/**
 * 学员-分组(StudentGroup)分页查询学员-分组响应
 *
 * @author makejava
 * @since 2022-03-28 16:07:15
 */
@Data
public class StudentGroupPageResponse extends StudentGroup {

    @ApiModelProperty(value = "条数")
    private int count;
}

