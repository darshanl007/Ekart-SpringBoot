package org.dars.ekart.service;

import org.dars.ekart.dto.Vendor;
import org.dars.ekart.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;

@Service
public class VendorService {

	@Autowired
	VendorRepository vendorRepository;

	public String loadRegister(Vendor vendor, ModelMap map) {
		map.put("vendor", vendor);
		return "vendor-register.html";
	}

	public String vendorRegister(@Valid Vendor vendor, BindingResult result) {

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
			vendor.setVerified(false);
			vendorRepository.save(vendor);
			return "home.html";
		}
	}

}
