package org.dars.ekart.service;

import java.util.Random;

import org.dars.ekart.dto.Vendor;
import org.dars.ekart.helper.EmailSender;
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
	EmailSender emailSender;

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
			session.setAttribute("failure", "OTP Missmatch");
			return "redirect:/vendor/otp/" + vendor.getId();
		}
	}

}