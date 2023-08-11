package cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2021-11-18 16:46:12
 */
@Data
public class CourseStudentDTO{

    @ApiModelProperty(value = "【职工ID】")
    private Long studentId;

}
