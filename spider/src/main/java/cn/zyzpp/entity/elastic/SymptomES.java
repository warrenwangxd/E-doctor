package cn.zyzpp.entity.elastic;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * Create by yster@foxmail.com 2018/8/4/004 19:30
 */
@Document(indexName = "medical", type = "node")
public class SymptomES {
    private Long id;
    private String name;//症状名
    private String ill_name;//疾病名
    private List<String> part;//部位
    private List<String> family;//科室
    private String initial; //首字母

    public SymptomES() {
    }

    public SymptomES(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getIll_name() {
        return ill_name;
    }

    public void setIll_name(String ill_name) {
        this.ill_name = ill_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getPart() {
        return part;
    }

    public void setPart(List<String> part) {
        this.part = part;
    }

    public List<String> getFamily() {
        return family;
    }

    public void setFamily(List<String> family) {
        this.family = family;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

}
