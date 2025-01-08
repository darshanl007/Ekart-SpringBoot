package org.dars.ekart.service;

import java.util.List;

import org.dars.ekart.dto.Product;
import org.dars.ekart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Service
public class AdminService {

	@Value("${admin.email}")
	String adminEmail;

	@Value("${admin.password}")
	String adminPassword;

	@Autowired
	ProductRepository productRepository;

	public String adminLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
		if (email.equals(adminEmail)) {
			if (password.equals(adminPassword)) {
				session.setAttribute("admin", adminEmail);
				session.setAttribute("success", "Login Success as Admin");
				return "redirect:/admin/home";
			} else {
				session.setAttribute("failure", "Invalid Password");
				return "redirect:/admin/login";
			}
		} else {
			session.setAttribute("failure", "Invalid Email");
			return "redirect:/admin/login";
		}
	}

	public String loadAdminHome(HttpSession session) {
		if (session.getAttribute("admin") != null) {
			return "admin-home.html";
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/admin/login";
		}
	}

	public String adminLogout(HttpSession session) {
		session.removeAttribute("admin");
		session.setAttribute("success", "Logged out Success");
		return "redirect:/";
	}

	public String approveProducts(HttpSession session, ModelMap map) {
		if (session.getAttribute("admin") != null) {
			List<Product> products = productRepository.findAll();
			if (products.isEmpty()) {
				session.setAttribute("failure", "No Products Present");
				return "redirect:/admin/home";
			} else {
				map.put("products", products);
				return "admin-products.html";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/admin/login";
		}
	}

	public String changeStatus(@PathVariable int id, HttpSession session) {
		if (session.getAttribute("admin") != null) {
			Product product = productRepository.findById(id).get();
			if (product.isApproved())
				product.setApproved(false);
			else
				product.setApproved(true);

			productRepository.save(product);
			session.setAttribute("success", "Product Status Changed Success");
			return "redirect:/admin/approve-products";
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/admin/login";
		}
	}

}
