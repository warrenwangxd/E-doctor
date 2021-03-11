package cn.zyzpp.matchword.service;

import cn.zyzpp.matchword.entity.SymptomES;

import java.util.List;

public interface MedicalService {
    void save(SymptomES medical);

    List<SymptomES> findAllByNameLike(String name);

    List<SymptomES> findAllByPartLike(String name);

    void delete(long id);

    void deleteAll();

    Long count();

    List<SymptomES> findAllByPartLike(String word, String zm);
}
