//package cn.qbs.wa.teach.organization.tdmq;
//
//import com.qbs.tdmq.TdmqProperties;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//@Slf4j
//@Component
//@Order(value = 1)
//public class StartListener implements CommandLineRunner {
//
//    @Autowired
//    private TdmqProperties tdmqProperties;
//
//
//    @Override
//    public void run(String... args) {
////        log.info("系统已初始化完成...");
//        new Timer().schedule(
//            new TimerTask() {
//                @Override
//                public void run() {
//                    tdmqProperties.setStartConsuming(true);
//                }
//            },
//            3000L
//        );
//    }
//
//}
