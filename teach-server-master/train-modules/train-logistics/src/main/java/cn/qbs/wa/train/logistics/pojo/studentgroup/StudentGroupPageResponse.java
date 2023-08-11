package cn.qbs.wa.train.logistics.pojo.studentgroup;

import cn.qbs.wa.train.logistics.entity.StudentGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

