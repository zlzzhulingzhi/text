package cn.qbs.wa.teach.course.standard.pojo.tcoursestudent;

import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程-预报名学员(TCourseStudent)分页查询课程-预报名学员响应
 *
 * @author makejava
 * @since 2022-10-27 14:46:00
 */
@Data
public class TCourseStudentPageResponse extends TCourseStudent {

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "报名时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signUpTime;

}

