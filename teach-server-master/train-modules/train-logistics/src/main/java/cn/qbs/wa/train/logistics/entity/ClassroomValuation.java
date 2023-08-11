package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-11-24 10:52:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClassroomValuation extends Model {

    private static final long serialVersionUID = 770304561545679398L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "收费项代号")
    private String itemCode;

    @ApiModelProperty(value = "收费项名称")
    private String itemName;

    @ApiModelProperty(value = "单价(元 · 每天)")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "备注")
    private String remark;

}
