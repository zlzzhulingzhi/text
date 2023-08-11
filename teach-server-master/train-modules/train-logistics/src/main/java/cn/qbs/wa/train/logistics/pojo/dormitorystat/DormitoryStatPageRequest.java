package cn.qbs.wa.train.logistics.pojo.dormitorystat;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.math.BigDecimal;

/**
 * 宿舍房型统计(DormitoryStat)分页查询参数
 *
 * @author makejava
 * @since 2023-06-05 11:10:10
 */
@Data
public class DormitoryStatPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "教室单元(字典值)")
    private String building;

    @ApiModelProperty(value = "房型名称")
    private String roomTypeName;
    
    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

}

