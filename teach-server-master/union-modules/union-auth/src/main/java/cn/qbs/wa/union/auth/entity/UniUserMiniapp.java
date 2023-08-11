package cn.qbs.wa.union.auth.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-10-21 08:35:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniUserMiniapp extends Model {

    private static final long serialVersionUID = -60975938978790241L;

    @ApiModelProperty(value = "关联 uni_user 表主键")
    private Long id;

    @ApiModelProperty(value = "小程序用户ID")
    private String openId;

}
