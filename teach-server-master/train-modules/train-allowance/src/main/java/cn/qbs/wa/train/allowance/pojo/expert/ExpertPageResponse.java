package cn.qbs.wa.train.allowance.pojo.expert;

import cn.qbs.wa.train.allowance.entity.Expert;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 专家信息(Expert)分页查询专家信息响应
 *
 * @author makejava
 * @since 2022-10-31 18:47:30
 */
@Data
public class ExpertPageResponse extends Expert {
    @ApiModelProperty(value = "性别")
    private String esex;
}

