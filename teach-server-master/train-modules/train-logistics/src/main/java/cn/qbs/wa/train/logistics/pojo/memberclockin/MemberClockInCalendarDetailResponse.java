package cn.qbs.wa.train.logistics.pojo.memberclockin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class MemberClockInCalendarDetailResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "打卡目标地点编号")
    private String siteCode;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "打卡日期",hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate clockInDate;

    @ApiModelProperty(value = "打卡时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;



}

