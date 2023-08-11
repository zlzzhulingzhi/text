package cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 机构插件表(WOrg)分页查询机构插件表响应
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@Data
public class WLecturerPageResultDTO {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id",hidden = true)
    private Long orgId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "头衔")
    private String title;


    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;




}

