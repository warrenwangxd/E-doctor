package cn.zyzpp.repository.elastic;


import cn.zyzpp.entity.elastic.SymptomES;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value ="elasticM" )
public interface MedicalESRepository extends ElasticsearchRepository<SymptomES,Long> {
    List<SymptomES> findAllByNameLike(String name, Pageable pageable);

}
