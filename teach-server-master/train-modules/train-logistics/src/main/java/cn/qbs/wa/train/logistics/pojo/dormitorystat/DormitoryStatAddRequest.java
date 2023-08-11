package cn.qbs.wa.train.logistics.pojo.dormitorystat;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 宿舍房型统计(DormitoryStat)创建宿舍房型统计参数
 *
 * @author makejava
 * @since 2023-06-05 11:10:10
 */
@Data
public class DormitoryStatAddRequest {

    @NotBlank(message = "请选择所属单元")
    @ApiModelProperty(value = "单元(字典值)")
    private String building;

    @NotBlank(message = "请填写房型编号")
    @ApiModelProperty(value = "房型编号")
    private String roomTypeCode;

    @NotBlank(message = "请填写房型名称")
    @ApiModelProperty(value = "房型名称")
    private String roomTypeName;

    @NotNull(message = "请填写房间总数")
    @Range(max = 10000L, message = "请填写合法的维修数量")
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

    @Digits(integer = 6, fraction = 2, message = "请填写合法的面积数值")
    @ApiModelProperty(value = "面积数值，单位平方米")
    private BigDecimal area;
    
    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    @Range(max = 10000L, message = "请填写合法的排序数值")
    private Integer sort;
}

