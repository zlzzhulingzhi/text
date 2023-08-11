package cn.qbs.wa.teach.organization.pojo.student;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学员(Student)分页查询学员响应
 *
 * @author WX
 * @since 2022-08-15 19:43:52
 */
@Data
public class StudentGroupList {

    @ApiModelProperty(value = "【主键】")
    private Long groupId;

    @ApiModelProperty(value = "【标签名称】")
    private String groupName;

}

