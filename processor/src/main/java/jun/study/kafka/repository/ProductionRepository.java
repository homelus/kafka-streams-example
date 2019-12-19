package jun.study.kafka.repository;

import jun.study.kafka.model.Production;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionRepository extends CrudRepository<Production, Long> {

}
