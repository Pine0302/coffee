package geektime.spring.data.datasourcedemo.repository;


import geektime.spring.data.datasourcedemo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
