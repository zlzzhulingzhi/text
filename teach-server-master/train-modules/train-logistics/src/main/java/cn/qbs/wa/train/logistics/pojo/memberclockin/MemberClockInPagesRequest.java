package cn.qbs.wa.train.logistics.pojo.memberclockin;


import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 学员打卡记录(MemberClockIn)分页查询参数
 *
 * @author makejava
 * @since 2022-12-26 15:42:22
 */
@Data
public class MemberClockInPagesRequest extends BasePageRequest {

    @ApiModelProperty(value = "打卡目标地点编号",example = "ss【宿舍】 jxl【教学楼】")
    private String siteCode;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "学员用户ID列表")
    private List<Long> memberIdList;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "打卡日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate clockInDate;

}

