package cn.zyzpp.matchword.service.impl;

import cn.zyzpp.matchword.entity.SymptomES;
import cn.zyzpp.matchword.repository.SymptomESRepository;
import cn.zyzpp.matchword.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Create by yster@foxmail.com 2018/8/4/004 19:36
 */
@Service
@CacheConfig(cacheNames = {"match"})
public class MedicalServiceImpl implements MedicalService {
    @Autowired
    private SymptomESRepository medicalRepository;

    @Override
    public void save(SymptomES medical){
        medicalRepository.save(medical);
    }

    @Override
    @Cacheable(key = "targetClass + methodName +#p0")
    public List<SymptomES> findAllByNameLike(String name){
        return medicalRepository.findAllByNameLike(name,new PageRequest(0,20));
    }

    @Override
    @Cacheable(key = "targetClass + methodName +#p0")
    public List<SymptomES> findAllByPartLike(String name){
        return medicalRepository.findAllByPartLike(name);
    }

    @Override
    public void delete(long id){
        medicalRepository.delete(id);
    }

    @Override
    public void deleteAll(){
        medicalRepository.deleteAll();
    }

    @Override
    public Long count(){
        return medicalRepository.count();
    }

    @Override
    @Cacheable(key = "targetClass +#p0 +#p1+ methodName")
    public List<SymptomES> findAllByPartLike(String word, String zm) {
        List<SymptomES> partLike = new ArrayList<SymptomES>();
        partLike.addAll(findAllByPartLike(word));
        Iterator<SymptomES> iterator = partLike.iterator();
        while (iterator.hasNext()){
            SymptomES next = iterator.next();
            if (!zm.equals(next.getInitial())){
                iterator.remove();
            }
        }
        return partLike;
    }

}
