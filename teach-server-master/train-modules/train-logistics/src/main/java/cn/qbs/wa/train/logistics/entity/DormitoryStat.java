package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2023-06-05 10:59:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DormitoryStat extends Model {

    private static final long serialVersionUID = -91890012189047630L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "教室单元(字典值)")
    private String building;

    @ApiModelProperty(value = "房型编号")
    private String roomTypeCode;

    @ApiModelProperty(value = "房型名称")
    private String roomTypeName;

    @ApiModelProperty(value = "房间总数")
    private Integer roomNum;

    @ApiModelProperty(value = "维修数量")
    private Integer maintNum;

    @ApiModelProperty(value = "在住数量")
    private Integer usingNum;

    @ApiModelProperty(value = "预计到达")
    private Integer incomeNum;

    @ApiModelProperty(value = "预计离店")
    private Integer outcomeNum;

    @ApiModelProperty(value = "面积数值，单位平方米")
    private Integer area;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
