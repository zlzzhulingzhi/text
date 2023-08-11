package cn.qbs.wa.teach.basic.api.pojo.DTO.app;

import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.TreeMenuDTO;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/9 17:19
 */
@Data
public class ApplicationFullDetailDTO {

    Long appId;

    String appName;

    String permission;

    List<TreeMenuDTO> menuList;

    String iconUrl;

    public Boolean getFilter() {
        return CollectionUtils.isEmpty(menuList);
    }



}
