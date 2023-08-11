package cn.qbs.wa.union.auth.pojo.uniuser;


import cn.qbs.wa.union.auth.entity.UniUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniUserDTO extends UniUser {

    private static final long serialVersionUID = -11819718494231879L;

    private Long userId;

    private Long userid;

    private String userName;

    public String getUserName() {
        return getRealName();
    }

    public Long getUserid() {
        return getId();
    }

    public Long getUserId() {
        return getId();
    }
}
