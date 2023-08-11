package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.entity.ClazzStudentSign;
import cn.qbs.wa.train.logistics.pojo.clazzsign.SignInRecordPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazzsign.SignInRecordResponse;
import cn.qbs.wa.train.logistics.pojo.clazzsign.SignInSearchRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 班级学员签到表(ClazzStudentSign)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-26 10:38:28
 */
public interface ClazzStudentSignMapper extends BaseMapper<ClazzStudentSign> {

    List<ClazzStudentSign> list(@Param("params") SignInSearchRequest search);

    IPage<ClazzStudent> pageStudentOut(Page<ClazzStudent> page, @Param("clazzId") Long clazzId);

    List<ClazzStudent> listStudentOut(@Param("clazzId") Long clazzId);

    IPage<SignInRecordResponse> pageSignInRecord(Page<?> mpPage, @Param("params") SignInRecordPageRequest request);

    List<SignInRecordResponse> signInRecordList(@Param("params") SignInRecordPageRequest request);
}

