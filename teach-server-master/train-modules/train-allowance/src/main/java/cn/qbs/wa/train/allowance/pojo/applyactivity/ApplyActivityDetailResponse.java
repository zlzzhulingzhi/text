package cn.qbs.wa.train.allowance.pojo.applyactivity;

import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.entity.ApplyActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)资助资金申请-学术会议和竞赛活动详情
 *
 * @author makejava
 * @since 2022-11-03 19:27:16
 */
@Data
public class ApplyActivityDetailResponse extends Apply {

    @ApiModelProperty(value = "学术活动数据")
    private ApplyActivity applyActivity;

}

