package cn.qbs.wa.train.logistics.pojo.clazz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.Clazz;

/**
 * 班级表(Clazz)分页查询班级表响应
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@Data
public class ClazzPageResponse extends Clazz {

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "员工姓名")
    private String realName;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "学员实际人数")
    private Integer studentCount;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "负责教师手机号")
    private String phone;
}

