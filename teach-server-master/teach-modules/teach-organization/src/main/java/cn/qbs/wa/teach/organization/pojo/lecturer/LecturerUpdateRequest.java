package cn.qbs.wa.teach.organization.pojo.lecturer;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * 讲师表(Lecturer)更新讲师表参数
 *
 * @author makejava
 * @since 2021-11-17 11:25:34
 */
@Data
public class LecturerUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ApiModelProperty(value = "讲师姓名")
    private String lecturerName;

    @ApiModelProperty(value = "头衔")
    private String title;

    @ApiModelProperty(value = "简介")
    @Length(max = 300, message = "讲师简介限定在300字以内")
    private String intro;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "【排序数组】")
    private Integer sort;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

}

