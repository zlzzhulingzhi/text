package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.exam.admin.pojo.dto.orgdesk.OrgDeskTaskExamInfoDTO;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 考试表(Exam)表机构前台h5服务接口
 *
 * @author WX
 * @since 2022-03-16 16:27:51
 */
public interface OrgDeskTaskExamService extends IService<Exam> {

    /**
     * 查询考试信息
     * @param params
     * @return
     */
    List<OrgDeskTaskExamInfoDTO> getExamList(IdListAndUserIdRequest params);

}

