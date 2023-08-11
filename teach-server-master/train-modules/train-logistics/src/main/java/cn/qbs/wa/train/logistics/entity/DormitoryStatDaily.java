package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author makejava
 * @since 2023-06-05 10:59:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DormitoryStatDaily extends Model {

    private static final long serialVersionUID = -12740037200554267L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "宿舍每日统计导入记录ID")
    private Long dormitoryStatImportId;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate day;

    @ApiModelProperty(value = "房型编号")
    private String roomTypeCode;

    @ApiModelProperty(value = "剩余房数")
    private Integer free;

}
