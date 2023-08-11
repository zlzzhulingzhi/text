package cn.qbs.wa.teach.question.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author zcm
 * @Date 2022/1/17 19:09
 * @Version 1.0
 */
public class StreamUtil {

    /**
     * Java8 Stream分割list集合（固定每页数量），list 数量越多，分的页也越多
     * @param list 集合数据
     * @param splitSize 几个分割一组
     * @return 集合分割后的集合
     */
    public static <T> List<List<T>> splitListByPageSize(List<T> list, int splitSize) {
        //判断集合是否为空
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        //计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;
        //开始分割
        return Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> list.parallelStream().skip(a * splitSize).limit(splitSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Java8 Stream分割list集合（固定页数），list 数量越多，每分的数量也越多
     * @param list 集合数据
     * @param pages 几个分割一组
     * @return 集合分割后的集合
     */
    public static <T> List<List<T>> splitListByPages(List<T> list, int pages) {
        //判断集合是否为空
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        //计算分割后的大小
        int pageSize = list.size() % pages == 0 ? list.size() / pages : list.size() / pages + 1;
        //开始分割
        return Stream.iterate(0, n -> n + 1)
                .limit(pageSize)
                .parallel()
                .map(a -> list.parallelStream().skip(a * pageSize).limit(pageSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 99; i++) {
            list.add(i + 1);
        }

        List<List<Integer>> listList1 = splitListByPages(list, 10);
        List<List<Integer>> listList2 = splitListByPageSize(list, 30);
        System.out.println();
    }

}
