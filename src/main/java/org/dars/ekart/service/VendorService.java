package org.dars.ekart.service;

import org.dars.ekart.dto.Vendor;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class VendorService {

	public String loadRegister(Vendor vendor, ModelMap map) {
		map.put("vendor", vendor);
		return "vendor-register.html";
	}

}
