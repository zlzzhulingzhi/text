package cn.qbs.wa.teach.basic.api.pojo.DTO.navigation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yjx
 */
@Data
public class NavigationItemDTO {

    private Long id;

    private Long parentId;

    private String name;

    private String code;

    private Integer type;

    private String uri;

    private String permission;

    private Integer sort;

    private Integer enabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
