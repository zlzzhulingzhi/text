package cn.qbs.wa.teach.basic.pojo.rolemenu;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 16:19
 */
@Data
public class RoleMenuVO {

    String menuName;

    String menuUri;

    String permission;

    String category;

    Long id;

    Long parentId;

    Long appId;

    Integer sort;


}
