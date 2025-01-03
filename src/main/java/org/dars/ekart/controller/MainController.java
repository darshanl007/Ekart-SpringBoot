package org.dars.ekart.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Value("${admin.email}")
	String adminEmail;

	@Value("${admin.password}")
	String adminPassword;

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

}
