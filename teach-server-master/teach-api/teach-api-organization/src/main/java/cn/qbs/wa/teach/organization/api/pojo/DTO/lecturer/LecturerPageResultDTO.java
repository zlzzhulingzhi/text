package cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 讲师表(Lecturer)分页查询讲师表响应
 *
 * @author makejava
 * @since 2021-11-17 11:25:34
 */
@Data
public class LecturerPageResultDTO  {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【机构名称】")
    private String orgName;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【手机号码】")
    private String account;

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "头衔")
    private String title;

    @ApiModelProperty(value = "0未添加 1已添加")
    private Integer added=0;

    @ApiModelProperty(value = "讲师姓名")
    private String lecturerName;





}

