package cn.qbs.wa.teach.exam.admin.mapper;

import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamPageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamPageResponse;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 考生表(Examinee)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeMapper extends BaseMapper<Examinee> {

    IPage<ExamPageResponse> page(@Param("page") IPage<?> page, @Param("params") ExamPageRequest params);

}

