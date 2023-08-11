package cn.qbs.wa.train.logistics.pojo.clazz;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 班级表(Clazz)分页查询参数
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@Data
public class ClazzDetailRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long id;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

}

