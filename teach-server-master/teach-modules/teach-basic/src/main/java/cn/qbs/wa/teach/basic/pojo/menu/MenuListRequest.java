package cn.qbs.wa.teach.basic.pojo.menu;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 16:19
 */
@Data
public class MenuListRequest {

    List<Long> menuIdList;

    Integer enabled;

}
