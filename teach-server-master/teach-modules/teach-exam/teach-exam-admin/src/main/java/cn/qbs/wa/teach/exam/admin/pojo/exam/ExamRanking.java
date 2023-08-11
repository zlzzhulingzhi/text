package cn.qbs.wa.teach.exam.admin.pojo.exam;


import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.teach.common.security.enums.SensitiveTypeEnum;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @Author zcm
 * @Date 2021-12-27 10:11
 * @Version 1.0
 */
@Data
public class ExamRanking {

    @ExcelIgnore
    @ApiModelProperty(value = "")
    private Long id;

    @ExcelIgnore
    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ExcelIgnore
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ExcelIgnore
    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ExcelIgnore
    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ExcelProperty("姓名")
    @ApiModelProperty(value = "考生姓名")
    private String examineeName;

//    @ExcelProperty("部门")
//    @ApiModelProperty(value = "部门名称")
//    private String deptName;

    @ExcelProperty("手机号")
    @ApiModelProperty(value = "手机号")
    @EncodeContent(desensitized = true, desensitizedType = SensitiveTypeEnum.MOBILE_PHONE)
    private String mobile;

    @ExcelProperty("排名")
    @ApiModelProperty(value = "排名")
    private Integer ranking;

    @ExcelProperty("分数")
    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

    @ExcelIgnore
    @ApiModelProperty(value = "考试用时多少时间完成(秒数)")
    private Integer useDuration;

    @ExcelProperty("用时")
    @ApiModelProperty(value = "考试用时格式化")
    private String useDurationFormat;

}
