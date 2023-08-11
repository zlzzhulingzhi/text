package cn.qbs.wa.teach.organization.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-01-19 14:05:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class News extends Model {

    private static final long serialVersionUID = 280206707919164275L;


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "新闻标题")
    private String title;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "首页展示 0 否，1：是")
    private Integer topping;

    @ApiModelProperty(value = "滚动展示 0 否，1：是")
    private Integer rolling;

    @ApiModelProperty(value = "新闻类型 0 系统新闻 1外部新闻")
    private Integer type;

    @ApiModelProperty(value = "打开链接")
    private String openUrl;

    @ApiModelProperty(value = "新闻分组")
    private String group;

    @ApiModelProperty(value = "新闻内容")
    private String content;

    @ApiModelProperty(value = "封面图片地址")
    private String coverUrl;

    @ApiModelProperty(value = "置顶排序")
    private Integer sort;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Integer updateBy;

    @ApiModelProperty(value = "新闻来源")
    private String newsSource;

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "浏览量")
    private Integer views;

}
