package org.dars.ekart.controller;

import java.util.List;

import org.dars.ekart.dto.Product;
import org.dars.ekart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Value("${admin.email}")
	String adminEmail;

	@Value("${admin.password}")
	String adminPassword;

	@Autowired
	ProductRepository productRepository;

	@GetMapping("/")
	public String loadHome() {
		return "home.html";
	}

	@GetMapping("/admin/login")
	public String loadAdminLogin() {
		return "admin-login.html";
	}

	@PostMapping("/admin/login")
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

	@GetMapping("/admin/home")
	public String loadAdminHome(HttpSession session) {
		if (session.getAttribute("admin") != null) {
			return "admin-home.html";
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/admin/login";
		}
	}

	@GetMapping("/admin/logout")
	public String adminLogout(HttpSession session) {
		session.removeAttribute("admin");
		session.setAttribute("success", "Logged out Success");
		return "redirect:/";
	}

	@GetMapping("/admin/approve-products")
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

	@GetMapping("/admin/changes/{id}")
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
