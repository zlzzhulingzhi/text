package cn.qbs.wa.teach.exam.answer.mapper;

import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageResponse;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考生表(Examinee)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeMapper extends BaseMapper<Examinee> {

    List<ExamPageResponse> selectAllIds(@Param("params") ExamPageRequest examPageRequest);

    Integer selectJoinTest(Long id);

    ExamPageResponse selectComplete(ExamPageResponse record);

    IPage<ExamPageResponse> selectUnfinished(@Param("page") Page<?> mpPage, @Param("list") List<ExamPageResponse> records,@Param("info")ExamPageRequest examPageRequest);

    IPage<ExamPageResponse> selectAccomplish(@Param("page")Page<?> mpPage,@Param("list") List<ExamPageResponse> records,@Param("info")ExamPageRequest examPageRequest);

    ExamPageResponse selectUnderway(ExamPageResponse record);


    List<ExamPageResponse> selectNotStart(@Param("list") List<ExamPageResponse> records,@Param("info")ExamPageRequest examPageRequest);

    ExamPageResponse  selectAnswer(@Param("list") ExamPageResponse record,@Param("info")ExamPageRequest examPageRequest,@Param("flag") int flag);

    List<ExamPageResponse> selectParticulars(ExamPageResponse record);

    void decrRemainingTimes(Long id);
}

