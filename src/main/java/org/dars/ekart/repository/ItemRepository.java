package org.dars.ekart.repository;

import org.dars.ekart.dto.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
