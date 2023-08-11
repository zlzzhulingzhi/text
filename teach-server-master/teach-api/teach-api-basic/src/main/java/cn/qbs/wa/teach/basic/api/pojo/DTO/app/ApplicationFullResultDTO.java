package cn.qbs.wa.teach.basic.api.pojo.DTO.app;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 16:55
 */
@Data
public class ApplicationFullResultDTO {

    String appTypeName;

    Long appTypeId;

    List<ApplicationFullDetailDTO> applicationList;
}
