package org.dars.ekart.controller;

import org.dars.ekart.dto.Customer;
import org.dars.ekart.dto.Order;
import org.dars.ekart.service.CustomerService;
import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping("/register")
	public String loadRegister(Customer customer, ModelMap map) {
		return customerService.loadRegister(customer, map);
	}

	@PostMapping("/register")
	public String CustomerRegister(@Valid Customer customer, BindingResult result, HttpSession session) {
		return customerService.CustomerRegister(customer, result, session);
	}

	@GetMapping("/otp/{id}")
	public String loadOtp(@PathVariable int id, ModelMap map) {
		map.put("id", id);
		return "customer-otp.html";
	}

	@PostMapping("/otp")
	public String verifyOtp(@RequestParam int id, @RequestParam int otp, HttpSession session) {
		return customerService.verifyOtp(id, otp, session);
	}

	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, HttpSession session) {
		return customerService.resendOtp(id, session);
	}

	@GetMapping("/login")
	public String loadLogin() {
		return "customer-login.html";
	}

	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
		return customerService.login(email, password, session);
	}

	@GetMapping("/home")
	public String loadHome(HttpSession session) {
		if (session.getAttribute("customer") != null) {
			return "customer-home.html";
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/customer/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("customer");
		session.setAttribute("success", "Logged Out Success");
		return "redirect:/";
	}

	@GetMapping("/view-products")
	public String viewProducts(HttpSession session, ModelMap map) {
		return customerService.viewProducts(session, map);
	}

	@GetMapping("/search-products")
	public String search(HttpSession session) {
		return customerService.search(session);
	}

	@PostMapping("/search-products")
	public String search(@RequestParam String query, HttpSession session, ModelMap map) {
		return customerService.search(query, session, map);
	}

	@GetMapping("/view-cart")
	public String viewCart(HttpSession session, ModelMap map) {
		return customerService.viewCart(session, map);
	}

	@GetMapping("/add-cart/{id}")
	public String addToCart(@PathVariable int id, HttpSession session) {
		return customerService.addToCart(id, session);
	}

	@GetMapping("/increase/{id}")
	public String increase(@PathVariable int id, HttpSession session) {
		return customerService.increase(id, session);
	}

	@GetMapping("/decrease/{id}")
	public String decrease(@PathVariable int id, HttpSession session) {
		return customerService.decrease(id, session);
	}

	@GetMapping("/remove/{id}")
	public String removeFromCart(@PathVariable int id, HttpSession session) {
		return customerService.removeFromCart(id, session);
	}

	@GetMapping("/payment")
	public String payment(HttpSession session, ModelMap map) {
		return customerService.payment(session, map);
	}

	@PostMapping("/success")
	public String paymnetSuccess(Order order, HttpSession session) {
		return customerService.paymentSuccess(order, session);
	}

	@GetMapping("/view-orders")
	public String viewOrders(HttpSession session, ModelMap map) {
		return customerService.viewOrders(session, map);
	}
}
