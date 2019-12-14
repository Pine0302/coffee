package geektime.spring.data.datasourcedemo.repository;


import geektime.spring.data.datasourcedemo.model.CoffeeOrder;
import org.springframework.data.repository.CrudRepository;

public interface CoffeeRepository extends BaseRepository<CoffeeOrder, Long> {
}
