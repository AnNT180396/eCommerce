package com.udacity.jdnd.course4.ecommerce.service;

import com.udacity.jdnd.course4.ecommerce.entities.Cart;
import com.udacity.jdnd.course4.ecommerce.entities.User;
import com.udacity.jdnd.course4.ecommerce.entities.UserOrder;
import com.udacity.jdnd.course4.ecommerce.repository.OrderRepository;
import com.udacity.jdnd.course4.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<UserOrder> createOrderForUser(String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            logger.error("User not found.");
            return ResponseEntity.notFound().build();
        }

        UserOrder order = createOrderFromCart(user.getCart());
        orderRepository.save(order);
        logger.error("Create user's order success.");
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<List<UserOrder>> getOrdersOfUser(String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            logger.error("User not found.");
            return ResponseEntity.notFound().build();
        }

        List<UserOrder> userOrderList = orderRepository.findByUser(user);
        logger.info("Get order success");
        return ResponseEntity.ok(userOrderList);
    }

    private UserOrder createOrderFromCart(Cart cart) {
        UserOrder order = new UserOrder();
        order.setItems(cart.getItems());
        order.setTotal(cart.getTotal());
        order.setUser(cart.getUser());
        return order;
    }
}
