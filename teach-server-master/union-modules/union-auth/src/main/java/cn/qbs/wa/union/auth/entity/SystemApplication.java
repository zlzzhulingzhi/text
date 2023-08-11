package cn.qbs.wa.union.auth.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author makejava
 * @since 2022-07-08 09:02:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemApplication extends Model {

    private static final long serialVersionUID = -48567650776957028L;


    @ApiModelProperty(value = "系统id")
    private Long id;

    @ApiModelProperty(value = "应用名称")
    private String name;

    @ApiModelProperty(value = "访问地址")
    private String url;

    @ApiModelProperty(value = "应用编码")
    private String code;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "说明")
    private String represent;

    @ApiModelProperty(value = "主机名称")
    private String hostName;

    @ApiModelProperty(value = "状态")
    private Integer enable;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String iconUrl;

}
