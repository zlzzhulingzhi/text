package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 执行异步方法
 *
 * @author yjx
 */
@Async
@Slf4j
@Service
public class AsyncServiceImpl {

    /**
     * 注销类型
     */
    public enum LogOutType {
        /**
         * 机构
         */
        ORG,

        /**
         * 用户
         */
        USER,

        /**
         * 职员
         */
        EMPLOYEE,

        /**
         * 学员
         */
        STUDENT,
        ;
    }

    @Resource
    private RedisService redisService;

    public void loggingOff(LogOutType type, Long targetId) {
        // 查询出 redis 登录用户的所有 key
        Set<String> keys = redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        if (CollUtil.isNotEmpty(keys)) {
            ArrayList<String> list = new ArrayList<>(keys);
            int batchSize = 100;
            List<String> batchKeys = new ArrayList<>(batchSize);
            List<String> clearKeyList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                batchKeys.add(list.get(i));
                if ((i + 1) % batchSize == 0) {
                    List<String> tempList = getClearList(type, targetId, batchKeys);
                    if (tempList != null) {
                        clearKeyList.addAll(tempList);
                    }
                    batchKeys.clear();
                }
            }
            if (!batchKeys.isEmpty()) {
                List<String> tempList = getClearList(type, targetId, batchKeys);
                if (tempList != null) {
                    clearKeyList.addAll(tempList);
                }
            }

            // 清除 redis key
            if (!clearKeyList.isEmpty()) {
                log.info("remove keys ==> {}", clearKeyList);
                redisService.deleteObject(clearKeyList);
            }
        }
    }

    private List<String> getClearList(LogOutType type, Long targetId, List<String> batchKeys) {
        List<String> clearList = null;
        List<LoginUser> cacheObjectList = redisService.getMultiCacheObject(batchKeys);
        // 遍历 key 所对应的内容是否存在对应的 targetId，存在则删除此条记录
        if (CollUtil.isNotEmpty(cacheObjectList)) {
            if (LogOutType.ORG.equals(type)) {
                clearList = extractStream(cacheObjectList, e -> targetId.equals(e.getOrgId()));
            } else if (LogOutType.USER.equals(type)) {
                clearList = extractStream(cacheObjectList, e -> targetId.equals(e.getUserid()));
            } else if (LogOutType.EMPLOYEE.equals(type)) {
                clearList = extractStream(cacheObjectList, e -> targetId.equals(e.getEmployeeId()));
            } else if (LogOutType.STUDENT.equals(type)) {
                clearList = extractStream(cacheObjectList, e -> targetId.equals(e.getStudentId()));
            }
        }
        return clearList;
    }

    private List<String> extractStream(List<LoginUser> cacheObjectList, Predicate<? super LoginUser> predicate) {
        return cacheObjectList.stream().filter(predicate).map(e -> CacheConstants.LOGIN_TOKEN_KEY + e.getToken()).collect(Collectors.toList());
    }
}
