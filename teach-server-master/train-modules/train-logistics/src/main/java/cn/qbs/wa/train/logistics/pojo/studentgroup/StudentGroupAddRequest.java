package cn.qbs.wa.train.logistics.pojo.studentgroup;


import cn.qbs.wa.train.logistics.pojo.student.StudentIdAndUserIdRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 学员-分组(StudentGroup)创建学员-分组参数
 *
 * @author makejava
 * @since 2022-03-28 16:07:15
 */
@Data
public class StudentGroupAddRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "学员id")
    private Long studentId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "标签id集合")
    private List<Long> groupIds;

    @ApiModelProperty(value = "学员id集合")
    private List<StudentIdAndUserIdRequest> studentIdList;

    @ApiModelProperty(value = "标识,0:选中,1:未选中")
    private Integer flag;

}

