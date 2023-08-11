package cn.qbs.wa.train.logistics.pojo.wlecturer;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.train.logistics.entity.Lecturer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 插件-讲师表(WLecturer)分页查询插件-讲师表响应
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
@Data
public class WLecturerPageResponse extends Lecturer {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String phone;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String account;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "机构名称")
    private String orgName;



}

