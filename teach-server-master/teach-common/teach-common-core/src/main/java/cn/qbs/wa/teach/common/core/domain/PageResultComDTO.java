package cn.qbs.wa.teach.common.core.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 搜索分页结果类
 *
 * @Author zcm
 * @Date 2021/11/4 15:16
 * @Version 1.0
 */
@Data
public class PageResultComDTO<T> implements Serializable {

    private static final long serialVersionUID = 8545996863226528791L;

    List<T> records;

    long total;

    long size;

    long current;

    long getPages() {
        if (this.getSize() == 0L) {
            return 0L;
        } else {
            long pages = this.getTotal() / this.getSize();
            if (this.getTotal() % this.getSize() != 0L) {
                ++pages;
            }

            return pages;
        }
    }




}
