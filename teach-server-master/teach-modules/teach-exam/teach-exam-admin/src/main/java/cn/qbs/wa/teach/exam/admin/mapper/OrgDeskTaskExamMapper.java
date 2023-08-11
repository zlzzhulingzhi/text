package cn.qbs.wa.teach.exam.admin.mapper;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.exam.admin.pojo.dto.orgdesk.OrgDeskTaskExamInfoDTO;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 考试表(Exam)表机构前台h5数据库访问层
 *
 * @author WX
 * @since 2022-03-16 16:04:51
 */
public interface OrgDeskTaskExamMapper extends BaseMapper<Exam> {


    /**
     * 查询考试信息
     * @param params
     * @param params
     * @return
     */
    List<OrgDeskTaskExamInfoDTO> getExamList(@Param("params") IdListAndUserIdRequest params);


}

