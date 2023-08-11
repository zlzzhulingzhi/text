package cn.qbs.wa.teach.exam.admin.pojo.tcert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: NQY
 * @Date: 2022/5/19 17:18
 * @Description:
 */
@Data
public class AwardRecordPageRequest {

    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;
}
