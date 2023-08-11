package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @Author WX
 * @Date 2021-12-24 18:44
 * @Version 1.0
 */
@Data
public class ExamAssignUserRequest {

    @ApiModelProperty(value = "指派用户集")
    private List<ExamUserRequest> examAssignUserList;

}
