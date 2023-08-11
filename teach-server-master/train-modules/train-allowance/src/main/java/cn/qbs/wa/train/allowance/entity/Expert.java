package cn.qbs.wa.train.allowance.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-10-31 18:47:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Expert extends Model {

    private static final long serialVersionUID = 799771999980957914L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "身份号码")
    private String idNumber;

    @ApiModelProperty(value = "出声年月")
    @JsonFormat(pattern = "yyyy-MM-dd ", timezone = "GMT+8")
    private LocalDate birthday;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "学历(字典值)")
    private String education;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "政治面貌(字典值)")
    private String politics;

    @ApiModelProperty(value = "参加工作时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate workDate;

    @ApiModelProperty(value = "通讯地址")
    private String address;

    @ApiModelProperty(value = "开户行")
    private String bank;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "所学专业")
    private String major;

    @ApiModelProperty(value = "毕业院校")
    private String school;

    @ApiModelProperty(value = "工作单位")
    private String workUnit;

    @ApiModelProperty(value = "职称(字典值)")
    private String title;

    @ApiModelProperty(value = "职位(字典值)")
    private String post;

    @ApiModelProperty(value = "评审成就")
    private String achievement;

    @ApiModelProperty(value = "个人成绩")
    private String performance;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
