package cn.qbs.wa.train.allowance.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author makejava
 * @since 2022-11-02 11:20:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyQualification extends Model {

    private static final long serialVersionUID = 283066840057886453L;

    @ApiModelProperty(value = "资质申请表ID")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

}
