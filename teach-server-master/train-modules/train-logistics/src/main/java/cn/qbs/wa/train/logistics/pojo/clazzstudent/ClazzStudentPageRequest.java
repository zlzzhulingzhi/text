package cn.qbs.wa.train.logistics.pojo.clazzstudent;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 班级学员表(ClazzStudent)分页查询参数
 *
 * @author makejava
 * @since 2022-10-08 17:28:15
 */
@Data
public class ClazzStudentPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "学员ID")
    private Long studentId;

    @ApiModelProperty(value = "学员ID")
    private Long memberId;

}

