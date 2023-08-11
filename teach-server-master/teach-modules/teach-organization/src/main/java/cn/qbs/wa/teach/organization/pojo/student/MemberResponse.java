package cn.qbs.wa.teach.organization.pojo.student;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 学员(Student)分页查询学员响应
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class MemberResponse {

    @ApiModelProperty(value = "【主键】")
    private Long memberId;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "【0不可用 1可用】")
    private Integer enabled;

    @ApiModelProperty(value = "当前班级")
    private String clazzName;

    private List<MemberClazzResponse> children = Collections.emptyList();
}

