package cn.qbs.wa.teach.course.api.pojo.DTO.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 【课程】(Course)分页查询【课程】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Data
public class CoursePageResultDTO {

    private static final long serialVersionUID = -5436189856674693205L;

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【课程简介】")
    private String introduction;

    @ApiModelProperty(value = "【上架状态 0下架 1上架】")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【上架时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shelfTime;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 0-未启用 1-已启用】")
    private Integer enabled;


    @ApiModelProperty(value = "创建人")
    private String realName;

    @ApiModelProperty(value = "报名人数")
    private Integer signUpNum;

    @ApiModelProperty(value = "所属机构")
    private String orgName;


    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【最大报名人数 0-无限制】")
    private Integer signUpLimit;

    @ApiModelProperty(value = "直播开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "直播结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "打开链接")
    private String linkUrl;

    @ApiModelProperty(value = "0未添加 1已添加")
    private Integer added=0;

    @ApiModelProperty(value = "【精品课程 1-精品课程 0-普通课程(默认)】")
    private Integer gooded;
}

