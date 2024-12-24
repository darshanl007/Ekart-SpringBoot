package org.dars.ekart.controller;

import org.dars.ekart.dto.Vendor;
import org.dars.ekart.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendor")
public class VendorController {

	@Autowired
	VendorService vendorService;

	@GetMapping("/register")
	public String loadRegister(Vendor vendor, ModelMap map) {
		return vendorService.loadRegister(vendor,map);
	}
}
