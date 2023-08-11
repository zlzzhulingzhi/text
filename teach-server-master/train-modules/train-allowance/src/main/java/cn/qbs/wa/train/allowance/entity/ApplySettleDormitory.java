package cn.qbs.wa.train.allowance.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author makejava
 * @since 2022-10-14 15:05:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplySettleDormitory extends Model {

    private static final long serialVersionUID = 148657893917897371L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "入驻申请ID")
    private Long applySettleId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始日期")
    private LocalDate useDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束日期")
    private LocalDate useDateEnd;

    @ApiModelProperty(value = "房型")
    private String roomType;

    @ApiModelProperty(value = "房间数量")
    private Integer roomNum;

    @ApiModelProperty(value = "备注")
    private String remark;

}
