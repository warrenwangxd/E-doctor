package cn.zyzpp.repository.elastic;

import cn.zyzpp.entity.elastic.SymptomES;
import cn.zyzpp.entity.medical.Medical;
import cn.zyzpp.repository.medical.MedicalRepository;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ESMedicalRepositoryTest {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Resource(name = "elasticM")
    private MedicalESRepository medicalESRepository;
    @Autowired
    private MedicalRepository medicalRepositoryMysql;

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        //先删除全部
        medicalESRepository.deleteAll();
        List<Medical> mysqlAll = medicalRepositoryMysql.findAll();
        for (Medical medical : mysqlAll){
            for(String symptom: medical.getSymptom_list()) {
                SymptomES m = new SymptomES(medical.getId(),symptom);
                m.setPart(medical.getPart());
                m.setFamily(medical.getFamily());
                m.setIll_name(medical.getName());
                String[] strings = new String[0];
                try {
                    strings = PinyinHelper.toHanyuPinyinStringArray(symptom.charAt(0),getHanyuPinyinOutputFormat());
                    m.setInitial(strings[0].substring(0,1));
                } catch (Exception badHanyuPinyinOutputFormatCombination) {
                    //badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                medicalESRepository.save(m);
            }
        }
        long end = System.currentTimeMillis();
        logger.info("总共耗时："+(end-start)/1000/60+"分钟");
    }

    private HanyuPinyinOutputFormat getHanyuPinyinOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //拼音大写
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        //无音标方式；WITH_TONE_NUMBER：1-4数字表示英标；WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //用v表示ü
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        return format;
    }

}