package cn.qbs.wa.train.logistics.pojo.memberclockin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学员打卡记录(MemberClockIn)更新学员打卡记录参数
 *
 * @author makejava
 * @since 2022-12-26 15:42:24
 */
@Data
public class MemberClockInUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;


    @ApiModelProperty(value = "备注")
    private String remark;

}

