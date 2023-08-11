package cn.qbs.wa.teach.basic.pojo.app;

import cn.qbs.wa.teach.basic.pojo.menu.TreeMenuResponse;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/9 17:19
 */
@Data
public class ApplicationFullDetailResponse {

    Long appId;

    String appName;

    String permission;

    List<TreeMenuResponse> menuList;

    Boolean filter;

    String iconUrl;

    public Boolean getFilter() {
        return CollectionUtils.isEmpty(menuList);
    }
}
