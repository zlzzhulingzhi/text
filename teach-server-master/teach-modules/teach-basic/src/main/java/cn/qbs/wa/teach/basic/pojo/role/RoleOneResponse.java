package cn.qbs.wa.teach.basic.pojo.role;

import cn.qbs.wa.teach.basic.entity.Role;
import cn.qbs.wa.teach.basic.pojo.menu.TreeMenuResponse;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 11:21
 */
@Data
public class RoleOneResponse extends Role {

    List<TreeMenuResponse> menu;
}
