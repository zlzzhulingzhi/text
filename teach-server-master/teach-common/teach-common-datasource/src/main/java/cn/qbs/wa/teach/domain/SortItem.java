package cn.qbs.wa.teach.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcm
 * @Date 2022/4/27 16:16
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortItem implements Serializable {

    /**
     * 需要进行排序的字段
     */
    private String column;
    /**
     * 顺序，默认 asc
     */
    @Getter(AccessLevel.NONE)
    private String order = "asc";

    public static SortItem asc(String column) {
        return build(column, "asc");
    }

    public static SortItem desc(String column) {
        return build(column, "desc");
    }

    public static List<SortItem> ascs(String... columns) {
        return Arrays.stream(columns).map(SortItem::asc).collect(Collectors.toList());
    }

    public static List<SortItem> descs(String... columns) {
        return Arrays.stream(columns).map(SortItem::desc).collect(Collectors.toList());
    }

    private static SortItem build(String column, String order) {
        return new SortItem(column, order);
    }

    public String getOrder() {
        if (!"desc".equalsIgnoreCase(order)) {
            return "asc";
        }
        return order;
    }

}

