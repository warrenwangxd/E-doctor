package cn.zyzpp.spider;

import cn.zyzpp.entity.medical.Medical;
import cn.zyzpp.repository.medical.MedicalRepository;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderServiceTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpiderMedicalService spiderMedicalService;

    /**
     * 58
     */
    int MAX = SpiderMedicalService.size;

    /**
     * 多线程爬取
     */
    @Test
    public void test() {
        List<Future<Integer>> resultList = new ArrayList<Future<Integer>>();
        for (int i = 0; i < MAX; i++) {
            //20~30的科室已经爬取过.
            if (i >= 20 && i < 30) {
                continue;
            }
            resultList.add(spiderMedicalService.crawlAndSave(i));
        }
        //判断所有线程都完成后，退出主进程.
        boolean notFinished = true;
        while (notFinished) {
            notFinished = false;
            for (Future<Integer> result : resultList) {
                if (!result.isDone()) {
                    notFinished =true;
                    break;
                }
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}