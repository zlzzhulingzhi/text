package cn.qbs.wa.teach.exam.api.pojo.DTO.orgdesk;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 机构前台h5任务详情中包含的考试
 *
 * @author WX
 * @version 1.0
 * @date 2022-03-16 16:15:18
 */
@Data
public class ExamInfoDTO {

    @ApiModelProperty(value = "考试id")
    private Long id;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "状态  1 未考试 2 考试中 3考试结束")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止（结束）时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "考试模式 1-标准模式 2-高级模式")
    private Integer examMode;
}
