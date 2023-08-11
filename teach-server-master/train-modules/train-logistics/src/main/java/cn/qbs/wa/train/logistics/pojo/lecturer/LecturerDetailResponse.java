package cn.qbs.wa.train.logistics.pojo.lecturer;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.train.logistics.entity.Lecturer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 讲师表(Lecturer)讲师表详情
 *
 * @author makejava
 * @since 2021-11-17 11:25:34
 */
@Data
public class LecturerDetailResponse extends Lecturer {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String phone;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String account;

}

