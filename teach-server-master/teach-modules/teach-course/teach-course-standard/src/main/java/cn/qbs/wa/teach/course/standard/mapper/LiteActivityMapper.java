package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.LiteActivityResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface LiteActivityMapper {

    IPage<LiteActivityPageResponse> page(Page<?> mpPage, @Param("params") LiteActivityPageRequest request);

    LiteActivityResponse getById(Long id);
}
