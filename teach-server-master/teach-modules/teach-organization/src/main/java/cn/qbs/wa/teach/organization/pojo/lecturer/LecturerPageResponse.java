package cn.qbs.wa.teach.organization.pojo.lecturer;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.teach.organization.entity.Lecturer;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 讲师表(Lecturer)分页查询讲师表响应
 *
 * @author makejava
 * @since 2021-11-17 11:25:34
 */
@Data
public class LecturerPageResponse extends Lecturer {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    @EncodeContent
    private String phone;

    @ApiModelProperty(value = "【机构名称】")
    private String orgName;

    @ApiModelProperty(value = "0未添加 1已添加")
    private Integer added=0;

}

