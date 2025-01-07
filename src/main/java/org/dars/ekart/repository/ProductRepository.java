package org.dars.ekart.repository;

import java.util.List;

import org.dars.ekart.dto.Product;
import org.dars.ekart.dto.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByVendor(Vendor vendor);

	List<Product> findByApprovedTrue();

}
