package org.dars.ekart.dto;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Product {
	@Id
	@GeneratedValue(generator = "product_id")
	@SequenceGenerator(name = "product_id", initialValue = 1001, allocationSize = 1)
	private int id;
	@Size(min = 3, max = 15, message = "* Enter between 3~15 characters")
	private String name;
	@NotNull(message = "* Enter proper value")
	@DecimalMin(value = "49", message = "* Enter above 49rs")
	@DecimalMax(value = "100000", message = "* Enter below 1 lakh rs")
	private double price;
	@NotNull(message = "* Enter proper value")
	@Min(value = 1, message = "* Should be atleast one")
	@Max(value = 30, message = "* Maximum 30 is allowed")
	private int stock;
	@Size(min = 15, max = 100, message = "* Enter between 15-100 characters")
	private String description;
	private String imageLink;
	private String category;
	@Transient
	private MultipartFile image;

	@ManyToOne
	Vendor vendor;
}
