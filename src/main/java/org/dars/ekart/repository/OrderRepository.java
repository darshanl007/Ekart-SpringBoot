package org.dars.ekart.repository;

import java.util.List;

import org.dars.ekart.dto.Customer;
import org.dars.ekart.dto.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByCustomer(Customer customer);

}
