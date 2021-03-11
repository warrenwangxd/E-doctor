package cn.zyzpp.spider;

import cn.zyzpp.entity.medical.Medical;
import cn.zyzpp.repository.medical.MedicalRepository;
import cn.zyzpp.util.ProjectPath;
import cn.zyzpp.util.TexUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 症状类
 * Create by yster@foxmail.com 2018/8/13/013 17:04
 */
public abstract class AbstractSpiderServer {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final static String ALL = "all";
    private final static String index = "http://jb39.com";
    private final static String WORD = "util";

    /**
     * 爬疾病
     * @param href
     * @param name
     * @return
     * @throws Exception
     */
    protected Medical getMedical(String href, String name) throws Exception {
        Medical medical = new Medical();
        medical.setId(-1);
        medical.setName(name);
        Document document = SpiderUtil.getDocument(index + href);
        medical.setFamily(getFamily(document,href, ".jb-xx-ks","jb-ks"));
        medical.setPart(getFamily(document,href, ".jb-xx-bw","jb-bw"));
        Map<String, Object> intro = getBrief(href, null);
        medical.setIntro((String) intro.get(ALL));
        medical.setIntro_list((List<String>) intro.get(WORD));
        Map<String, Object> casue = getBrief(href, "/jibing-bingyin");
        medical.setCause((String) casue.get(ALL));
        medical.setCause_list((List<String>) casue.get(WORD));
        Map<String, Object> diagnose = getBrief(href, "/jibing-zhenduan");
        medical.setDiagnose((String) diagnose.get(ALL));
        medical.setDiagnose_list((List<String>) diagnose.get(WORD));
        Map<String, Object> cure = getBrief(href, "/jibing-zhiliao");
        medical.setCure((String) cure.get(ALL));
        medical.setCure_list((List<String>) cure.get(WORD));
        Map<String, Object> prevent = getBrief(href, "/jibing-yufang");
        medical.setPrevent((String) prevent.get(ALL));
        medical.setPrevent_list((List<String>) prevent.get(WORD));
        Map<String, Object> complication = getBrief(href, "/jibing-zhengzhuang");
        medical.setComplication((String) complication.get(ALL));
        medical.setComplication_list((List<String>) complication.get(WORD));
        Map<String, Object> symptom = getBrief(href, "/jibing-zhengzhuang");
        medical.setSymptom((String) symptom.get(ALL));
        medical.setSymptom_list((List<String>) symptom.get(WORD));
        return medical;
    }

    /**
     * 部位+科室
     *
     * @param href
     * @return
     */
    protected List<String> getFamily(Document document,String href, String clas, String by) throws Exception {
        Element first = document.select(".ul-ss-3").select(clas).first();
        if (first==null) {
            if (by!=null && document.getElementsByClass(by).first() != null){
                first = document.getElementsByClass(by).first();
            }else {
                logger.warn("无部位/科室 " + index + href);
            }
        }
        //是否有词
        Elements elements = first.getElementsByTag("a");
        if (elements.size() == 0) {
            logger.warn( "无关键词"+index + href);
            return null;
        }
        //遍历词
        List<String> symptomList = new ArrayList<>();
        for (Element element1 : elements) {
            symptomList.add(element1.text());
        }
        return symptomList;
    }


    /**
     * 症状并发症等含有通用词的
     *
     * @param href
     * @return
     */
    protected Map<String, Object> getBrief(String href, String word) throws Exception {
        String url = (word == null) ? (index + href) : (index + word + href.substring(href.lastIndexOf("/")));
        //症状详情页
        Document document = SpiderUtil.getDocument(url);
        Elements select = document.select("div.spider");
        if (select.size() == 0) {
            if (document.select("div.jb-body").size()!=0){
                select = document.select("div.jb-body");
            }else{
                logger.error("异常：详情页无详情 "+url);
            }
        }
        Element first = select.first();
        //爬取所有描述
        Map<String, Object> map = new HashMap<>();
        map.put(ALL, first.text());
        //判断是否有词
        Elements elements = first.getElementsByTag("a");
        if (elements.size()== 0) {
//            logger.warn("正常无spider<a> "+url);
            return map;
        }
        //遍历词
        List<String> symptomList = new ArrayList<>();
        for (Element element1 : elements) {
            symptomList.add(element1.text());
            //新的词链接
            String href1 = element1.attr("href");
            //保存新词到本地txt文件
            TexUtil.write(element1.text()+"\r\n"+href1+"\r\n",ProjectPath.getRootPath("/word_link.txt"));
        }
        map.put(WORD, symptomList);
        return map;
    }
}
