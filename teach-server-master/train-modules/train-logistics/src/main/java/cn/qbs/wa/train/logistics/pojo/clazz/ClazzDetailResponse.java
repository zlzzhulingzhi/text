package cn.qbs.wa.train.logistics.pojo.clazz;

import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.Clazz;

import java.util.List;

/**
 * 班级表(Clazz)班级表详情
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@Data
public class ClazzDetailResponse extends Clazz {

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【学生列表】")
    private IPage<ClazzStudentPageResponse> clazzStudentPageResponseList;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

}

