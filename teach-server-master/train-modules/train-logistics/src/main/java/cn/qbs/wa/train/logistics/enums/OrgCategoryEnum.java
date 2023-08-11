package cn.qbs.wa.train.logistics.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @description: TODO
 @author vieux
 @date 2021/11/10 15:07
 *
 */
@Getter
@AllArgsConstructor
public enum OrgCategoryEnum {



    ENTERPRISE(1, "企业"),
    COLLAGES(2, "高校"),
    TRAIN(3, "培训机构");


    @JsonValue
    private Integer catagory;

    private String name;

    public static List toList() {
        List list = Lists.newArrayList();
        OrgCategoryEnum[] values = OrgCategoryEnum.values();
        for (OrgCategoryEnum value : values) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", value.getCatagory());
            map.put("name", value.getName());
            list.add(map);
        }
        return list;
    }

    public static String getName(Integer catagory){
        OrgCategoryEnum[] values = OrgCategoryEnum.values();
        for (OrgCategoryEnum value : values) {
            if (value.catagory.equals(catagory)){
                return value.getName();
            }
        }
        return null;
    }


}
