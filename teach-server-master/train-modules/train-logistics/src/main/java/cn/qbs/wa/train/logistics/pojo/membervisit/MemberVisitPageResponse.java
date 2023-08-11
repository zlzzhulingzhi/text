package cn.qbs.wa.train.logistics.pojo.membervisit;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.MemberVisit;

/**
 * 学员访问管理(MemberVisit)分页查询学员访问管理响应
 *
 * @author makejava
 * @since 2022-12-28 16:24:22
 */
@Data
public class MemberVisitPageResponse extends MemberVisit {

    @ApiModelProperty(value = "机构名称")
    private String orgName;

}

