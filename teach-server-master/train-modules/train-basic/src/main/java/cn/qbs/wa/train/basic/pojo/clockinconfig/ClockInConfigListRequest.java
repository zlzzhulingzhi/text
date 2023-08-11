package cn.qbs.wa.train.basic.pojo.clockinconfig;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 打卡配置(ClockInConfig)创建打卡配置参数
 *
 * @author makejava
 * @since 2022-12-26 15:38:26
 */
@Data
public class ClockInConfigListRequest {

    @ApiModelProperty(value = "编号")
    private String siteCode;

}

