package cn.qbs.wa.train.logistics.pojo.lecturer;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 讲师表(Lecturer)创建讲师表参数
 *
 * @author makejava
 * @since 2021-11-17 11:25:34
 */
@Data
public class LecturerAddRequest {

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
}

