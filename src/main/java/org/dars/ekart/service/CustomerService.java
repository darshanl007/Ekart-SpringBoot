package org.dars.ekart.service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.dars.ekart.dto.Customer;
import org.dars.ekart.dto.Product;
import org.dars.ekart.helper.AES;
import org.dars.ekart.helper.EmailSender;
import org.dars.ekart.repository.CustomerRepository;
import org.dars.ekart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	EmailSender emailSender;

	public String loadRegister(Customer customer, ModelMap map) {
		map.put("customer", customer);
		return "customer-register.html";
	}

	public String CustomerRegister(@Valid Customer customer, BindingResult result, HttpSession session) {
		if (!customer.getPassword().equals(customer.getConfirmPassword()))
			result.rejectValue("confirmPassword", "error.confirmPassword",
					"* Password and Confirm Password Should Match");
		if (customerRepository.existsByEmail(customer.getEmail()))
			result.rejectValue("email", "error.email", "* Email Already Exists");
		if (customerRepository.existsByMobile(customer.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number Already Exists");
		if (result.hasErrors())
			return "customer-register.html";
		else {
			int otp = new Random().nextInt(100000, 1000000);
			customer.setOtp(otp);
			customer.setPassword(AES.encrypt(customer.getPassword()));
			customerRepository.save(customer);
			emailSender.send(customer);
			System.err.println(customer.getOtp());
			session.setAttribute("success", "Otp Sent Successfully");
			return "redirect:/customer/otp/" + customer.getId();
		}
	}

	public String verifyOtp(int id, int otp, HttpSession session) {
		Customer customer = customerRepository.findById(id).orElseThrow();
		if (customer.getOtp() == otp) {
			customer.setVerified(true);
			customerRepository.save(customer);
			session.setAttribute("success", "Customer Account Created Success");
			return "redirect:/";
		} else {
			session.setAttribute("failure", "OTP Missmatch");
			return "redirect:/customer/otp/" + customer.getId();
		}
	}

	public String resendOtp(int id, HttpSession session) {
		Customer customer = customerRepository.findById(id).orElseThrow();
		customer.setOtp(new Random().nextInt(100000, 1000000));
		customer.setVerified(false);
		customerRepository.save(customer);
		System.err.println(customer.getOtp());
		emailSender.send(customer);
		session.setAttribute("success", "OTP Resent Success");
		return "redirect:/vendor/otp/" + customer.getId();
	}

	public String login(String email, String password, HttpSession session) {
		Customer customer = customerRepository.findByEmail(email);
		if (customer == null) {
			session.setAttribute("failure", "Invalid Email");
			return "redirect:/customer/login";
		} else {
			if (AES.decrypt(customer.getPassword()).equals(password)) {
				if (customer.isVerified()) {
					session.setAttribute("customer", customer);
					session.setAttribute("success", "Login Success");
					return "redirect:/customer/home";
				} else {
					int otp = new Random().nextInt(100000, 1000000);
					customer.setOtp(otp);
					customerRepository.save(customer);
					emailSender.send(customer);
					System.err.println(customer.getOtp());
					session.setAttribute("success", "Otp Sent Successfully, First Verify Email for Logging In");
					return "redirect:/customer/otp/" + customer.getId();
				}
			} else {
				session.setAttribute("failure", "Invalid Password");
				return "redirect:/customer/login";
			}
		}
	}

	public String viewProducts(HttpSession session, ModelMap map) {
		if (session.getAttribute("customer") != null) {
			List<Product> products = productRepository.findByApprovedTrue();
			if (products.isEmpty()) {
				session.setAttribute("failure", "No Products Present");
				return "redirect:/customer/home";
			} else {
				map.put("products", products);
				return "customer-view-products.html";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}

	public String search(HttpSession session) {
		if (session.getAttribute("customer") != null) {
			return "customer-search.html";
		} else {
			session.setAttribute("failure", "Invalid Session, First Login");
			return "redirect:/customer/login";
		}
	}

	public String search(String query, HttpSession session, ModelMap map) {
		if (session.getAttribute("customer") != null) {
			String toSearch = "%" + query + "%";
			List<Product> list1 = productRepository.findByNameLike(toSearch);
			List<Product> list2 = productRepository.findByDescriptionLike(toSearch);
			List<Product> list3 = productRepository.findByCategoryLike(toSearch);
			HashSet<Product> products = new HashSet<Product>();
			products.addAll(list1);
			products.addAll(list2);
			products.addAll(list3);
			map.put("products", products);
			map.put("query", query);
			return "customer-search.html";
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/customer/login";
		}
	}

}
