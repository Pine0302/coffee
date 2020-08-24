package geektime.spring.data.datasourcedemo;

import geektime.spring.data.datasourcedemo.converter.MoneyReadConverter;
import geektime.spring.data.datasourcedemo.model.Coffee;
import geektime.spring.data.datasourcedemo.model.CoffeeOrder;
import geektime.spring.data.datasourcedemo.model.MongoCoffee;
import geektime.spring.data.datasourcedemo.model.OrderState;
import geektime.spring.data.datasourcedemo.repository.CoffeeOrderRepository;
import geektime.spring.data.datasourcedemo.repository.CoffeeRepository;
import geektime.spring.data.datasourcedemo.service.CoffeeOrderService;
import geektime.spring.data.datasourcedemo.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@Slf4j
@EnableCaching(proxyTargetClass = true)
public class DataSourceDemoApplication implements ApplicationRunner {
    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private CoffeeOrderRepository orderRepository;
    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
    }

    public static void main(String[] args) {
        SpringApplication.run(DataSourceDemoApplication.class, args);
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        //runMain();
        //runMongo();
        runCache();
    }

    public void runMain() {
        //get all coffee
        log.info("ALl Coffee: {}", coffeeRepository.findAll());

        //order a coffee and change order status
        Optional<Coffee> latte = coffeeService.findOneCoffee("latte");
        log.info("get a Coffee: {}", latte);

        if (latte.isPresent()) {
            CoffeeOrder order = coffeeOrderService.saveCoffeeOrder("pine", latte.get());
            log.info("saved order: {}", order);
            log.info("from init to paid: {}", coffeeOrderService.updateState(order, OrderState.PAID));
            log.info("from paid to init: {}", coffeeOrderService.updateState(order, OrderState.INIT));
        }

    }

    public void runMongo() {
        MongoCoffee latte = MongoCoffee.builder()
                .name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        MongoCoffee saved = mongoTemplate.save(latte);
        log.info("saved coffee: {}", saved);

        List<MongoCoffee> list = mongoTemplate.find(
                Query.query(Criteria.where("name").is("latte")), MongoCoffee.class
        );
        log.info("find { } Coffee", list.size());
        list.forEach(c -> log.info("Coffee {} ", c));
    }

    public void runCache() throws InterruptedException {
        //get all coffee
        log.info("ALl Coffee: {}", coffeeService.findAllCoffee().size());
        for (int i = 0; i < 10; i++) {
            log.info(" find from cache .");
            coffeeService.findAllCoffee();
        }
        //coffeeService.reloadCoffee();
        Thread.sleep(5_000);

		log.info(" find after reload .");
		coffeeService.findAllCoffee().forEach(c->log.info("coffee:{}",c.getName()));
    }


}

