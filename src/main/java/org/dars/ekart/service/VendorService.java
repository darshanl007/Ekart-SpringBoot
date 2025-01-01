package org.dars.ekart.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.dars.ekart.dto.Product;
import org.dars.ekart.dto.Vendor;
import org.dars.ekart.helper.AES;
import org.dars.ekart.helper.CloudinaryHelper;
import org.dars.ekart.helper.EmailSender;
import org.dars.ekart.repository.ProductRepository;
import org.dars.ekart.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class VendorService {

	@Autowired
	VendorRepository vendorRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	EmailSender emailSender;

	@Autowired
	CloudinaryHelper cloudinaryHelper;

	public String loadRegister(Vendor vendor, ModelMap map) {
		map.put("vendor", vendor);
		return "vendor-register.html";
	}

	public String vendorRegister(@Valid Vendor vendor, BindingResult result, HttpSession session) {

		if (!vendor.getPassword().equals(vendor.getConfirmPassword()))
			result.rejectValue("confirmPassword", "error.confirmPassword",
					"* Password and Confirm Password Should Match");

		if (vendorRepository.existsByEmail(vendor.getEmail()))
			result.rejectValue("email", "error.email", "* Email Already Exists");

		if (vendorRepository.existsByMobile(vendor.getMobile()))
			result.rejectValue("mobile", "error.mobile", "Mobile Number Already Exists");

		if (result.hasErrors()) {
			return "vendor-register.html";
		}

		else {
			int otp = new Random().nextInt(100000, 1000000);
			vendor.setOtp(otp);
			vendor.setPassword(AES.encrypt(vendor.getPassword()));
			vendorRepository.save(vendor);
			emailSender.send(vendor);
			System.err.println(vendor.getOtp());
			session.setAttribute("success", "Otp Sent Successfully");
			return "redirect:/vendor/otp/" + vendor.getId();
		}
	}

	public String verifyOtp(int id, int otp, HttpSession session) {
		Vendor vendor = vendorRepository.findById(id).orElseThrow();
		if (vendor.getOtp() == otp) {
			vendor.setVerified(true);
			vendorRepository.save(vendor);
			session.setAttribute("success", "Vendor Account Created Successfully");
			return "redirect:/";
		} else {
			session.setAttribute("failure", "OTP Missmatch , Try Again");
			return "redirect:/vendor/otp/" + vendor.getId();
		}
	}

	public String resendOtp(int id, HttpSession session) {
		Vendor vendor = vendorRepository.findById(id).orElseThrow();
		vendor.setOtp(new Random().nextInt(100000, 1000000));
		vendor.setVerified(false);
		vendorRepository.save(vendor);
		System.err.println(vendor.getOtp());
		emailSender.send(vendor);
		session.setAttribute("success", "OTP Resent Success");
		return "redirect:/vendor/otp/" + vendor.getId();
	}

	public String login(String email, String password, HttpSession session) {
		Vendor vendor = vendorRepository.findByEmail(email);
		if (vendor == null) {
			session.setAttribute("failure", "Invalid Email");
			return "redirect:/vendor/login";
		} else {
			if (AES.decrypt(vendor.getPassword()).equals(password)) {
				if (vendor.isVerified()) {
					session.setAttribute("vendor", vendor);
					session.setAttribute("success", "Login Success");
					return "redirect:/vendor/home";
				} else {
					int otp = new Random().nextInt(100000, 1000000);
					vendor.setOtp(otp);
					vendorRepository.save(vendor);
					emailSender.send(vendor);
					System.err.println(vendor.getOtp());
					session.setAttribute("success", "Otp Sent Successfully, First Verify Email for Logging In");
					return "redirect:/vendor/otp/" + vendor.getId();
				}
			} else {
				session.setAttribute("failure", "Invalid Password");
				return "redirect:/vendor/login";
			}
		}
	}

	public String loadAddProduct(Product product, HttpSession session, ModelMap map) {
		if (session.getAttribute("vendor") != null) {
			map.put("product", product);
			return "add-product.html";
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/vendor/login";
		}
	}

	public String addProduct(@Valid Product product, BindingResult result, HttpSession session) throws IOException {
		if (session.getAttribute("vendor") != null) {
			if (result.hasErrors()) {
				return "add-product.html";
			} else {
				Vendor vendor = (Vendor) session.getAttribute("vendor");
				product.setVendor(vendor);
				product.setImageLink(cloudinaryHelper.saveToCloudinary(product.getImage()));
				productRepository.save(product);
				session.setAttribute("success", "Product Added Success");
				return "redirect:/vendor/home";
			}
		} else {
			session.setAttribute("faliure", "Invalid Session, Login Again");
			return "redirect:/vendor/login";
		}
	}

	public String manageProducts(HttpSession session, ModelMap map) {
		if (session.getAttribute("vendor") != null) {
			Vendor vendor = (Vendor) session.getAttribute("vendor");
			List<Product> products = productRepository.findByVendor(vendor);
			if (products.isEmpty()) {
				session.setAttribute("failure", "No Products Present");
				return "redirect:/vendor/home";
			} else {
				map.put("products", products);
				return "vendor-view-products.html";
			}
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/vendor/login";
		}
	}

}
