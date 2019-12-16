package geektime.spring.data.datasourcedemo;

import geektime.spring.data.datasourcedemo.model.Coffee;
import geektime.spring.data.datasourcedemo.model.CoffeeOrder;
import geektime.spring.data.datasourcedemo.model.OrderState;
import geektime.spring.data.datasourcedemo.repository.CoffeeOrderRepository;
import geektime.spring.data.datasourcedemo.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@Slf4j
public class DataSourceDemoApplication implements ApplicationRunner {
	@Autowired
	private CoffeeRepository coffeeRepository;
	@Autowired
	private CoffeeOrderRepository orderRepository;

	public static void main(String[] args) {
		SpringApplication.run(DataSourceDemoApplication.class, args);
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		initOrders();
		findOrders();
	}

	public void initOrders(){
		Coffee latte = Coffee.builder().name("latte")
				.price(Money.of(CurrencyUnit.of("CNY"),30.0))
				.build();
		coffeeRepository.save(latte);
		Coffee espersso = Coffee.builder().name("espersso")
				.price(Money.of(CurrencyUnit.of("CNY"),20))
				.build();
		coffeeRepository.save(espersso);
		log.info("Coffee: {}",latte);
		log.info("Coffee: {}",espersso);

		CoffeeOrder order1 = CoffeeOrder.builder()
				.customer("Pine")
				.items(Collections.singletonList(espersso))
				.state(OrderState.INIT)
				.build();
		orderRepository.save(order1);
		log.info("Order: {}",order1);

		CoffeeOrder order2 = CoffeeOrder.builder()
				.customer("song")
				.items(Arrays.asList(espersso,latte))
				.state(OrderState.INIT)
				.build();
		orderRepository.save(order2);
		log.info("Order2: {}",order2);
	}

	public void findOrders(){
		coffeeRepository
				.findAll(Sort.by(Sort.Direction.DESC,"id"))
				.forEach(c->log.info("Loading {} ",c));
		List<CoffeeOrder> list = orderRepository.findTop3ByOrderByUpdateTimeDescIdDesc();
		log.info("findTop3ByOrderByUpdateTimeDescIdc: {} ",getJoinedOrderId(list));

		list = orderRepository.findByCustomerOrderById("pine");
		log.info("findByCustomerOrderById: {} ",getJoinedOrderId(list));

		list.forEach(o->{
			log.info("Order {}",o.getId());
			o.getItems().forEach(i -> log.info(" Item {} ",i));
		});
		list = orderRepository.findByItems_Name("espresso");
		log.info("findByItems_Name: {}",getJoinedOrderId(list));
	}

	public String getJoinedOrderId(List<CoffeeOrder> list){
		return list.stream().map(o -> o.getId().toString()).collect(Collectors.joining(","));
	}

}

