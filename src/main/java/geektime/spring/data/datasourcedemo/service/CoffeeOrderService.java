package geektime.spring.data.datasourcedemo.service;

import geektime.spring.data.datasourcedemo.model.Coffee;
import geektime.spring.data.datasourcedemo.model.CoffeeOrder;
import geektime.spring.data.datasourcedemo.model.OrderState;
import geektime.spring.data.datasourcedemo.repository.CoffeeOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Service
@Transactional
public class CoffeeOrderService {
    @Autowired
    CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder saveCoffeeOrder(String customer, Coffee...coffee){
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .state(OrderState.INIT)
                .items(new ArrayList<>(Arrays.asList(coffee)))
                .build();
        CoffeeOrder savedOrder = coffeeOrderRepository.save(order);
        log.info("new Order: {}", savedOrder);
        return savedOrder;
    }

    public boolean updateState(CoffeeOrder coffeeOrder,OrderState state){
        if(state.compareTo(coffeeOrder.getState())<=0){
            log.info("wrong state");
            return false;
        }
        coffeeOrder.setState(state);
        coffeeOrderRepository.save(coffeeOrder);
        return true;
    }

}
