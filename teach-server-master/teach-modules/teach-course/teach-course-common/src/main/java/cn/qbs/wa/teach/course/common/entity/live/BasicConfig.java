package cn.qbs.wa.teach.course.common.entity.live;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-06-15 10:07:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicConfig extends Model {

    private static final long serialVersionUID = -45668636139431810L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
