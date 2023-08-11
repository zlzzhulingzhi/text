package cn.qbs.wa.teach.basic.api.pojo.DTO.menu;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 18:19
 */

@Data
public class MenuListDTO {


    List<Long> menuIdList;

    Integer enabled;
}
