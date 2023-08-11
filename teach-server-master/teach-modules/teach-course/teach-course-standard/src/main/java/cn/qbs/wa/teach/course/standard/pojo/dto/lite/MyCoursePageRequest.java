package cn.qbs.wa.teach.course.standard.pojo.dto.lite;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MyCoursePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;


    @ApiModelProperty(value = "用户ID", hidden = true)
    private Long memberId;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

}