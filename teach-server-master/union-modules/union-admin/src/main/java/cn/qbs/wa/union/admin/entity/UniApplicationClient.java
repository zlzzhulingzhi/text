package cn.qbs.wa.union.admin.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniApplicationClient extends Model {

    private static final long serialVersionUID = -82563729384387281L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "")
    private String clientCode;

    @ApiModelProperty(value = "")
    private String clientName;

}
