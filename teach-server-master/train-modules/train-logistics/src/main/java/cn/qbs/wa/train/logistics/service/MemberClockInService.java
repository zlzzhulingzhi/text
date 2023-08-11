package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.MemberClockIn;
import cn.qbs.wa.train.logistics.pojo.clazz.LiteClazzResponse;
import cn.qbs.wa.train.logistics.pojo.memberclockin.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 学员打卡记录(MemberClockIn)表服务接口
 *
 * @author makejava
 * @since 2022-12-26 15:42:22
 */
public interface MemberClockInService extends IService<MemberClockIn> {

    /**
     * 新增学员打卡记录
     * @param params
     * @return
     */
    boolean add(MemberClockInAddRequest params);

    /**
     * 分页查询学员打卡记录
     * @param params
     * @return
     */
    IPage<MemberClockInPageResponse> page(MemberClockInPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    MemberClockInDetailResponse detail(Serializable id);

    /**
     * 更新学员打卡记录
     * @param params
     * @return
     */
    boolean update(MemberClockInUpdateRequest params);

    /**
     * 删除学员打卡记录
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    IPage<MemberClockInPageResponse> pageMember(MemberClockInSelectPageRequest params);

    List<MemberClockInCalendarResponse> calendarList(MemberClockInCalendarRequest params);
    MemberClockInCalendarDetailOverViewResponse calendarDetail(MemberClockInCalendarDetailRequest params);
    List<LiteClazzResponse> selectClazzList();

    IPage<MemberClockInPageResponse> pages(MemberClockInPagesRequest params);
}

