package geektime.spring.data.datasourcedemo;

import geektime.spring.data.datasourcedemo.model.Coffee;
import geektime.spring.data.datasourcedemo.model.CoffeeOrder;
import geektime.spring.data.datasourcedemo.model.OrderState;
import geektime.spring.data.datasourcedemo.repository.CoffeeOrderRepository;
import geektime.spring.data.datasourcedemo.repository.CoffeeRepository;
import geektime.spring.data.datasourcedemo.service.CoffeeOrderService;
import geektime.spring.data.datasourcedemo.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@Slf4j
public class DataSourceDemoApplication implements ApplicationRunner {
	@Autowired
	private CoffeeRepository coffeeRepository;
	@Autowired
	private CoffeeOrderRepository orderRepository;
	@Autowired
	private CoffeeService coffeeService;
	@Autowired
	private CoffeeOrderService coffeeOrderService;


	public static void main(String[] args) {
		SpringApplication.run(DataSourceDemoApplication.class, args);
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		//get all coffee
		log.info("ALl Coffee: {}", coffeeRepository.findAll());

		//order a coffee and change order status
		Optional<Coffee> latte = coffeeService.findOneCoffee("latte");
		log.info("get a Coffee: {}", latte);

		if(latte.isPresent()){
			 CoffeeOrder order = coffeeOrderService.saveCoffeeOrder("pine",latte.get());
			 log.info("saved order: {}",order);
			 log.info("from init to paid: {}",coffeeOrderService.updateState(order, OrderState.PAID));
			 log.info("from paid to init: {}",coffeeOrderService.updateState(order, OrderState.INIT));
		}




	}


}

