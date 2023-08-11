package cn.qbs.wa.train.logistics.pojo.stat;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupDormitoryFreeDTO implements Serializable {

    /**
     * 房型类型编号
     */
    private String roomTypeCode;

    /**
     * 剩余房数
     */
    private Integer freeNum;

}

