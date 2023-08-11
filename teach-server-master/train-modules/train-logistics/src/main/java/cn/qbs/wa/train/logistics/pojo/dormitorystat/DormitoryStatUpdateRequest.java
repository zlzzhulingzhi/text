package cn.qbs.wa.train.logistics.pojo.dormitorystat;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 宿舍房型统计(DormitoryStat)更新宿舍房型统计参数
 *
 * @author makejava
 * @since 2023-06-05 11:10:10
 */
@Data
public class DormitoryStatUpdateRequest {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "主键")
    private Long id;
    
    @ApiModelProperty(value = "教室单元(字典值)")
    private String building;
    
    @ApiModelProperty(value = "房型编号")
    private String roomTypeCode;
    
    @ApiModelProperty(value = "房型名称")
    private String roomTypeName;

    @Range(max = 10000L, message = "请填写合法的房间总数")
    @ApiModelProperty(value = "房间总数")
    private Integer roomNum;

    @Range(max = 10000L, message = "请填写合法的维修数量")
    @ApiModelProperty(value = "维修数量")
    private Integer maintNum;
    
    @ApiModelProperty(value = "在住数量")
    private Integer usingNum;
    
    @ApiModelProperty(value = "预计到达")
    private Integer incomeNum;
    
    @ApiModelProperty(value = "预计离店")
    private Integer outcomeNum;

    @Digits(message = "请填写合法的面积数值", integer = 6, fraction = 2)
    @ApiModelProperty(value = "面积数值，单位平方米")
    private BigDecimal area;
    
    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    @Range(max = 10000L, message = "请填写合法的排序数值")
    private Integer sort;

}

