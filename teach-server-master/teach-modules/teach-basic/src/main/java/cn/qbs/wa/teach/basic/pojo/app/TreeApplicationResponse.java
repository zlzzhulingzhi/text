package cn.qbs.wa.teach.basic.pojo.app;

import cn.qbs.wa.teach.basic.pojo.menu.TreeMenuResponse;
import cn.qbs.wa.teach.common.core.utils.TreeNode;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/9 16:12
 */
@Data
public class TreeApplicationResponse extends TreeNode<TreeApplicationResponse> {

    String appName;

    TreeMenuResponse menu;
}
