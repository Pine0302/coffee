package geektime.spring.data.datasourcedemo.service;

import geektime.spring.data.datasourcedemo.model.Coffee;
import geektime.spring.data.datasourcedemo.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@Slf4j
@CacheConfig(cacheNames = "coffee")
public class CoffeeService {

    private static final String CACHE = "springbucks-coffee";

    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    public Optional<Coffee> findOneCoffee(String name) {
        HashOperations<String,String,Coffee> hashOperations = redisTemplate.opsForHash();
        if((redisTemplate.hasKey(CACHE))&&(hashOperations.hasKey(CACHE,name))){
            log.info("get coffee {} from redis.",name);
            return Optional.of(hashOperations.get(CACHE,name));
        }
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("coffee Found: {} ", coffee);
        if(coffee.isPresent()){
            log.info("put coffee {} into reids ",name);
            hashOperations.put(CACHE,name,coffee.get());
            redisTemplate.expire(CACHE,1, TimeUnit.MINUTES);
        }
        return coffee;
    }

    @Cacheable
    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    @CacheEvict
    public void reloadCoffee() {

    }


}
