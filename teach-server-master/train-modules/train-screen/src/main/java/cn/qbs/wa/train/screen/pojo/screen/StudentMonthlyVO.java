package cn.qbs.wa.train.screen.pojo.screen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentMonthlyVO implements Serializable {

    private String month;

    private Integer num;

}
