package cn.qbs.wa.teach.question.util;

/**
 * 加载对象接口
 * @Author zcm
 * @Date 2021/11/18 09:19
 * @Version 1.0
 */
@FunctionalInterface
public interface DataLoader<T> {

    /**
     * 加载对象
     * @return
     */
    T load();

}
