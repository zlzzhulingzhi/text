package cn.qbs.wa.teach.organization.pojo.student;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学员(Student)分页查询参数
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class MemberPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "课程专题")
    private String courseName;

}

