package cn.qbs.wa.teach.organization.pojo.lecturer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class LecturerResponse {

    @ApiModelProperty(value = "所属机构")
    private String orgName;

    @ApiModelProperty(value = "讲师ID")
    private Long lecturerId;

    @ApiModelProperty(value = "讲师名称")
    private String lecturerName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "当前班级")
    private String clazzName;

    private List<LecturerClazzResponse> children = Collections.emptyList();
}
