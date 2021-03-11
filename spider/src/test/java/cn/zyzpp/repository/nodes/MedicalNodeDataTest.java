/*
 * Author : 小东哥(warrenwangxd@foxmail.com)
 * 2021-03-08
 *
 */

package cn.zyzpp.repository.nodes;

import cn.zyzpp.entity.medical.Medical;
import cn.zyzpp.entity.nodes.BotNode;
import cn.zyzpp.entity.nodes.BotRelation;
import cn.zyzpp.repository.medical.MedicalRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Description： 初始化图节点与关系数据.
 * <p>
 * Author: 小东哥（warrenwangxd@foxmail.com）
 * Date: Created in 2021/3/8 16:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MedicalNodeDataTest {
    Logger logger = LoggerFactory.getLogger(getClass().getName());
    @Autowired
    BotRepository botRepository;
    @Autowired
    MedicalRepository medicalRepository;
    @Autowired
    BotRelationRepository botRelationRepository;

    final String DISEASE = "疾病";
    final String SYMPTOM = "症状";
    final String RELATOION = "产生";


    @Test
    public void test() {
        long start = System.currentTimeMillis();
        // botRepository.deleteAll();
        //读取数据库所有疾病并保存到Neo4j
        int num = 0;//当前第几页
        int size = 1000;//每页100条
        Sort sort = new Sort(Sort.Direction.DESC, "id");//降序
        Pageable pageable = new PageRequest(num++, size, sort);
        Page<Medical> medicalPage = medicalRepository.findAll(pageable);
        List<Medical> medicalList = medicalPage.getContent();
        saveOrUpdateNodes(medicalList);
        while (medicalPage.hasNext()) {
            pageable = new PageRequest(num++, size, sort);
            medicalPage = medicalRepository.findAll(pageable);
            saveOrUpdateNodes(medicalPage.getContent());
        }
        long end = System.currentTimeMillis();
        logger.info("总共耗时：" + (end - start) / 1000 / 60 + "分钟");
    }

    /**
     * 保存节点以及关系.
     *
     * @param medicalList
     */
    private void saveOrUpdateNodes(List<Medical> medicalList) {
        for (Medical medical : medicalList) {
            BotNode medicalNode = botRepository.findAllByName(medical.getName());
            if (medicalNode == null) {
                medicalNode = new BotNode(medical.getName(), DISEASE, medical.getFamily(), medical.getPart());
                botRepository.save(medicalNode); //保存疾病节点
                if (logger.isInfoEnabled()) {
                    logger.info("save medical: " + medical.getName());
                }
            } else {
                logger.info("Exists medicalNode: " + medical.getName());
            }


            for (String symptom : medical.getSymptom_list()) {
                BotNode symptomNode = botRepository.findAllByName(symptom);
                if (symptomNode == null) {
                    symptomNode = new BotNode(symptom, SYMPTOM, medical.getFamily(), medical.getPart());
                    botRepository.save(symptomNode); //保存症状节点
                    logger.info("save symptomNode: " + symptomNode);
                } else {
                    logger.info("Exists symptomNode: " + symptomNode);
                }
                List<BotRelation> botRelations = botRelationRepository.findBotRelationByStartAndEndNode(medicalNode.getName(), symptomNode.getName());
                if (botRelations.size() == 0) {
                    botRelationRepository.save(new BotRelation(medicalNode, symptomNode, RELATOION));
                    logger.info("save symptom: " + symptom);
                } else {
                    logger.info("Exists botRelation: " + medical.getName() + "->" + symptom);
                }
            }
        }

    }
}
