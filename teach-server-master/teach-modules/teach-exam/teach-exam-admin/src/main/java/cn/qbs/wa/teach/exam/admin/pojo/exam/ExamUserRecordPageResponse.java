package cn.qbs.wa.teach.exam.admin.pojo.exam;

import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentGroupListDTO;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试分页查询参数
 *
 * @Author WX
 * @Date 2022-08-15 13:49
 * @Version 1.0
 */
@Data
public class ExamUserRecordPageResponse {

    @ExcelIgnore
    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ExcelIgnore
    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ExcelIgnore
    @ApiModelProperty(value = "学员id")
    private Long studentId;

    @ExcelIgnore
    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ExcelIgnore
    @ApiModelProperty(value = "考生考试记录id")
    private Long recordId;

    @ExcelIgnore
    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ExcelProperty("学员姓名")
    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ExcelIgnore
    @ApiModelProperty(value = "标签集合")
    private List<StudentGroupListDTO> groupList;

    @ExcelIgnore
    @ApiModelProperty(value = "所在组织")
    private String deptName;

    @ExcelProperty("学员手机号")
    @ApiModelProperty(value = "学员手机号")
    private String phone;

    @ExcelProperty("开考时间")
    @ApiModelProperty(value = "开考时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ExcelProperty("开考时间")
    @ApiModelProperty(value = "交卷时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    @ExcelProperty("实际考试时长")
    @ApiModelProperty(value = "实际考试时长")
    private String useDurationFormat;

    @ExcelProperty("考试次数")
    @ApiModelProperty(value = "考试次数")
    private Integer examNum;

    @ExcelProperty("指派时间")
    @ApiModelProperty(value = "指派时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ExcelProperty("分数")
    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

}

