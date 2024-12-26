package org.dars.ekart.repository;

import org.dars.ekart.dto.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

	boolean existsByEmail(String email);

	boolean existsByMobile(long mobile);

}
