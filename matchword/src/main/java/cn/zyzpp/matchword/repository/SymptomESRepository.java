package cn.zyzpp.matchword.repository;

import cn.zyzpp.matchword.entity.SymptomES;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomESRepository extends ElasticsearchRepository<SymptomES,Long> {
    List<SymptomES> findAllByNameLike(String name, Pageable pageable);

    List<SymptomES> findAllByPartLike(String name);
}
