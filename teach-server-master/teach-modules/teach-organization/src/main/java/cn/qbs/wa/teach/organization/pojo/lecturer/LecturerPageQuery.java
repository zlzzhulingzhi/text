package cn.qbs.wa.teach.organization.pojo.lecturer;

import cn.qbs.wa.teach.domain.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LecturerPageQuery extends BasePageRequest {

    private Long orgId;

    private String lecturerName;

    private String courseName;

}
