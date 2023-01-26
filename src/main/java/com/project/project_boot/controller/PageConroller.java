package com.project.project_boot.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.project_boot.service.UserService;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageConroller {

	private final UserService userService;

	@GetMapping("/username")
	@RequestMapping("/")
	public String homepage(@Nullable Principal principal, @Nullable Model model) {

		if (principal != null) {

			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
			return "bodies/homepage";
		} else {
			model.addAttribute("guest", "guest");

		}
		return "bodies/homepage";
	}
	
	@RequestMapping("/login_form")
	public String login_form() {
		return "redirect:User/login_form";
	}

	@RequestMapping("/sign_up_form")
	public String sign_up_form() {
		return "redirect:/User/sign_up";
	}

	@RequestMapping("/mypage")
	public String mypage() {
		return "redirect:/User/mypage";
	}

	// post
	@RequestMapping("/modify_form")
	public String modify_form() {
		return "redirect:/User/modify_form";
	}

	@RequestMapping("/product_information_page")
	public String product_information_page() {
		return "redirect:/Product/product_information_page";
	}

	@RequestMapping("/success")
	public String paysuccess() {
		System.out.println("카카오페이 성공입니다.");

		return "open_win/success";
	}

	@RequestMapping("/fail")
	public String payfail() {
		System.out.println("카카오페이 실패입니다.");
		return "/";
	}

	@RequestMapping("/cancel")
	public String paycancel() {
		System.out.println("카카오페이 취소입니다.");
		return "/";
	}

	@RequestMapping("/inquiry_registration")
	public String inquiry_registration() {
		return "redirect:/User/inquiry_registration";
	}
	@GetMapping("/view_details")
	public String view_details() {
		return "/view_details";

	}


}
