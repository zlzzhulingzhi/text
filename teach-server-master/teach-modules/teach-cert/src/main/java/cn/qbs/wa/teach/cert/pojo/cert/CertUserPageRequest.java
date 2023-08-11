package cn.qbs.wa.teach.cert.pojo.cert;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 颁发记录(AwardRecord)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
@Data
public class CertUserPageRequest extends BasePageRequest {


    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "证书Id")
    private String phone;

    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    @ApiModelProperty(value = "部门数组Id")
    private List<Long> deptIdList;

    @ApiModelProperty(value = "身份证号码")
    private String idNum;

    @ApiModelProperty(value = "用户id数组")
    private List<Long> userIdList;

    @ApiModelProperty(value = "证书id")
    private Long certId;




}

