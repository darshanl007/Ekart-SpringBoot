package org.dars.ekart.repository;

import org.dars.ekart.dto.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	boolean existsByEmail(String email);

	boolean existsByMobile(long mobile);

	Customer findByEmail(String email);

}
