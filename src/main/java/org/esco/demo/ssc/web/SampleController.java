package org.esco.demo.ssc.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class SampleController {

	@RequestMapping("/login")
	public String login() {
		return "redirect:/";
	}

//	@RequestMapping("/j_spring_cas_security_logout")
//	public String spring_logout() {
//		return "redirect:/logout";
//	}

//	@RequestMapping("/logout")
//	public String logout() {
//		return "redirect:/";
//	}

	@GetMapping(value = { "/", "" })
	public String index(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		log.info("passing in /");
		model.addAttribute("user", user);

		// renders /WEB-INF/jsp/index.jsp
		return "index";
	}

	@GetMapping("/secure")
	public String secure(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		log.info("passing in /");
		model.addAttribute("user", user);

		// renders /WEB-INF/jsp/index.jsp
		return "secure/index";
	}

	@GetMapping("/filtered")
	public String filtered(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		log.info("passing in /");
		model.addAttribute("user", user);

		// renders /WEB-INF/jsp/index.jsp
		return "secure/admin/index";
	}

}
