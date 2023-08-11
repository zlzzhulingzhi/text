package cn.qbs.wa.teach.basic.api.pojo.DTO.role;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 17:23
 */
@Data
public class RoleMenuDTO {


    String menuName;

    String menuUri;

    String permission;

    String category;

    Long id;

    Long parentId;

    Long appId;

    Integer sort;
}
