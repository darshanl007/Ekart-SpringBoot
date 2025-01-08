package org.dars.ekart.controller;

import org.dars.ekart.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	AdminService adminService;

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
		return adminService.adminLogin(email, password, session);
	}

	@GetMapping("/admin/home")
	public String loadAdminHome(HttpSession session) {
		return adminService.loadAdminHome(session);
	}

	@GetMapping("/admin/logout")
	public String adminLogout(HttpSession session) {
		return adminService.adminLogout(session);
	}

	@GetMapping("/admin/approve-products")
	public String approveProducts(HttpSession session, ModelMap map) {
		return adminService.approveProducts(session, map);
	}

	@GetMapping("/admin/changes/{id}")
	public String changeStatus(@PathVariable int id, HttpSession session) {
		return adminService.changeStatus(id, session);
	}
}
