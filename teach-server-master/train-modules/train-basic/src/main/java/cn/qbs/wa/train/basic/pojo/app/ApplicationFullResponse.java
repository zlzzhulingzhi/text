package cn.qbs.wa.train.basic.pojo.app;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/9 17:19
 */
@Data
public class ApplicationFullResponse {

    String appTypeName;

    Long appTypeId;

    List<ApplicationFullDetailResponse> applicationList;

}
