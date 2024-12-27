package org.dars.ekart.controller;

import org.dars.ekart.dto.Vendor;
import org.dars.ekart.service.VendorService;
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
@RequestMapping("/vendor")
public class VendorController {

	@Autowired
	VendorService vendorService;

	@GetMapping("/register")
	public String loadRegister(Vendor vendor, ModelMap map) {
		return vendorService.loadRegister(vendor, map);
	}

	@PostMapping("/register")
	public String vendorRegister(@Valid Vendor vendor, BindingResult result, HttpSession session) {
		return vendorService.vendorRegister(vendor, result, session);
	}

	@GetMapping("/otp/{id}")
	public String loadOtp(@PathVariable int id, ModelMap map) {
		map.put("id", id);
		return "vendor-otp.html";
	}

	@PostMapping("/otp")
	public String verifyOtp(@RequestParam int id, @RequestParam int otp, HttpSession session) {
		return vendorService.verifyOtp(id, otp, session);
	}

	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, HttpSession session) {
		return vendorService.resendOtp(id, session);
	}

	@GetMapping("/login")
	public String loadLogin() {
		return "vendor-login.html";
	}

	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
		return vendorService.login(email, password, session);
	}

	@GetMapping("/home")
	public String loadHome(HttpSession session) {
		if (session.getAttribute("vendor") != null) {
			return "vendor-home.html";
		} else {
			session.setAttribute("failure", "Invalid Session, Login Again");
			return "redirect:/vendor/login";
		}
	}
}
