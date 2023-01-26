package com.project.project_boot.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.Encoder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.project_boot.constant.Role;
import com.project.project_boot.dto.InquiryDTO;
import com.project.project_boot.dto.KakaoDTO;
import com.project.project_boot.dto.UserDTO;
import com.project.project_boot.entity.Inquiry;
import com.project.project_boot.entity.KakaoResult;
import com.project.project_boot.entity.Member;
import com.project.project_boot.security.SecurityUser;
import com.project.project_boot.service.InquiryService;
import com.project.project_boot.service.KakaoService;
import com.project.project_boot.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/User")
@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final InquiryService inquiryService;
	private final KakaoService kakaoService;

	@GetMapping("/sign_up")
	public String sign_up_Form(Model model) {
		model.addAttribute("userDTO", new UserDTO());
		return "user_content/sign_up_form";
	}

	@PostMapping("/sign_up")
	public String sign_up(@Valid UserDTO userDTO, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "user_content/sign_up_form";
		}

		try {
			Member member = Member.createUser(userDTO, passwordEncoder);
			userService.saveMember(member);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			System.out.println(e.getMessage());
			return "/user_content/sign_up_form";
		}
		return "redirect:/";
	}
	

	@GetMapping("/login_form")
	public String login_form(HttpServletRequest req) {
		// 페이지 이동 할 때 로그인 해결
		return "user_content/login_form";
	}

	@GetMapping("/login/error")
	public String loginError(Model model) {
		model.addAttribute("errorMessage", "아이디 또는 비밀번호를 확인해주세요");
		return "user_content/login_form";
	}

	@GetMapping("/login/sucs")
	public String loginsucs() {
		return "user_content/login_sucs";
	}

	// 마이페이지 넣을 구간
	@GetMapping("/mypage")
	public String mypage_form(Principal principal, Model model) {
		if (principal == null) {
			return "user_content/login_form";
		} else {
			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
		}

		try {
			List<KakaoResult> KList = kakaoService.getPayResult(principal.getName());
			model.addAttribute("KList", KList);

			return "user_content/mypage_form";

		}

		catch (IllegalArgumentException e) {
			System.out.println("오류는 :" + e);
		}
		return "user_content/mypage_form";
	}

	// 취소
	@GetMapping("/cancellation_return_exchange_refund")
	public String cancellation() {
		return "user_content/cancellation_return_exchange_refund";
	}

	// 문의
	@GetMapping("/inquiry_form")
	public String inquiry(Model model,Principal principal,
			Authentication authentication) {
		if(authentication.getAuthorities().toString().equals("[ROLE_USER]")||
				authentication.getAuthorities().toString().equals("[ROLE_MANAGER]")) {
			List<Inquiry> u_info=inquiryService.findUserid(principal.getName());
			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
			model.addAttribute("inquiryList",u_info);
		}
		else if(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
			List<Inquiry> u_info=inquiryService.findAdmin("판매자 신청");
			model.addAttribute("inquiryList",u_info);
		}
		
		return "user_content/inquiry";
	}
	//판매자 신청 게시판
	@GetMapping("/admin_form")
	public String admin_f(Model model,Principal principal,
			Authentication authentication) {
		if(authentication.getAuthorities().toString().equals("[ROLE_USER]")) {
			List<Inquiry> u_info=inquiryService.findUserid(principal.getName());
			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
			model.addAttribute("inquiryList",u_info);
			
			return "bodies/homepage";
		}
		else if(authentication.getAuthorities().toString().equals("[ROLE_ADMIN]")) {
			List<Inquiry> u_info=inquiryService.seller_application("판매자 신청");
			model.addAttribute("inquiryList",u_info);
			model.addAttribute("admin","admin");
		}
		return "user_content/inquiry";
	}
	//판매자로 승격
	@RequestMapping("/rangking")
	public String rangking(Model model,Principal principal,
			Authentication authentication,
			@RequestParam("ranking_id")String user,@RequestParam("ranking_user")String rank) {
		if(rank.equals("MANAGER")) {
			userService.usermember(user);
		}
		
		return "user_content/inquiry";
	}

	// 문의 게시글 작성
	@GetMapping("/inquiry_resp")
	public String inquiry_response(Model model, Principal principal,@RequestParam("result")
	String result) {
		String u_id = principal.getName();
		model.addAttribute("userid", u_id);
		String name = userService.findingUserName(principal.getName());
		model.addAttribute("name", name);
		if(result.equals("apply")) {
		model.addAttribute("apply","apply");
		}
		return "user_content/inquiry_response";
	}

	@PostMapping("/inquiry_post")
	public String inquiry_post(@RequestParam("userid") String userid, @RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam("type") String type,
			InquiryDTO inquiryDTO,Model model)
			throws IOException {
		inquiryDTO = InquiryDTO.createInquiryDTO(userid, title, content, type);
		try {
			Inquiry inquiry = Inquiry.createinquiry(inquiryDTO);
			System.out.println("문의내역 올렸습니다.");
			inquiryService.InquiryImport(inquiry);
			List<Inquiry>InList=inquiryService.findUserid(userid);
			model.addAttribute("inquiryList", InList);
		} catch (IllegalStateException e) {

			System.out.println("문의 실패");
			return "user_content/inquiry";
		}
		
		return "user_content/inquiry";
	}

	// 문의 버튼
	@PostMapping("/inquiry_type")
	public String inquiry_type(@RequestParam List<String> select, Principal principal, Model model) {
		System.out.println("여기로 넘어옴");
		if (select != null) {
			for (String check : select) {
				List<Inquiry> InList = inquiryService.check(check, principal.getName());
				model.addAttribute("inquiryList", InList);
				System.out.println("가져오는거 성공함" + InList);
			}
		}
		return "user_content/inquiry";
	}

	// 어드민 문의 답변 게시판
	@GetMapping("/inquiry_answer_move")
	public String inquiry_answer_form(
			@RequestParam("number") Long seq_num,Model model) {
		List<Inquiry> user_post = inquiryService.findUserpost(seq_num);
		System.out.println(seq_num);
		for (Inquiry inquiry : user_post) {
			System.out.println(inquiry.toString());
		}
		model.addAttribute("ansList",user_post);
		return "admin_content/answer_form";
	}	

	@PostMapping("/inquiry_answer")
	public String inquiry_answer(@RequestParam("number") String number, @RequestParam("type") String type,
			@RequestParam("title") String title, @RequestParam("content") String content,
			@RequestParam("day") String day, @RequestParam("id") String id, @RequestParam("answer") String answer,
			InquiryDTO inquiryDTO) {
		int inq_num = Integer.parseInt(number);
		inquiryDTO = InquiryDTO.UpdateInquiryDTO(inq_num, id, title, content, type, answer);
		Inquiry inquiry = Inquiry.updateAnswer(inquiryDTO);
		System.out.println("문의내역 올렸습니다." + inquiry);
		inquiryService.InquiryImport(inquiry);
		return "user_content/inquiry";
	}
	@GetMapping("/inquiry_answer_get")
	public String inquiry_answer_get(@RequestParam("ans_number_get")Long seq_num,Model model)
	{
		List<Inquiry> user_post = inquiryService.findUserpost(seq_num);
		System.out.println(seq_num);
		for (Inquiry inquiry : user_post) {
			System.out.println(inquiry.toString());
		}
		model.addAttribute("ansList",user_post);
		return "user_content/inquiry_answer";
	}

	// 끝
	@GetMapping("/modify_form")
	public String modify_form(Model model, Principal principal) {
		String u_id = principal.getName();
		model.addAttribute("userid", u_id);
		return "user_content/modify";
	}

	@PostMapping("/modify")
	public String modify(@RequestParam("id") String user_id, @RequestParam("name_ch") String name_mod,
			@RequestParam("pw_ch") String pw_mod, @RequestParam("pw") String pass,
			@RequestParam("phone_ch") String phone_mod, @RequestParam("add_ch") String add_mod, UserDTO userDTO,
			Principal principal) {

		String pw = userService.findingUserpass(principal.getName());

		System.out.println(pw);
		System.out.println(pass);

		boolean check = passwordEncoder.matches(pass, userService.findingUserpass(principal.getName()));

		if (!check) {
			System.out.println("비밀번호 불일치");
			return "user_content/login_error";
		}
		System.out.println("비밀번호 일치");
		userDTO.setName(name_mod);
		userDTO.setPw(pw_mod);
		userDTO.setPhone(phone_mod);
		String pwencod = passwordEncoder.encode(pw_mod);
		System.out.println(pwencod);
		userDTO.setAddress(add_mod);
		Member mem = Member.createUser(userDTO, passwordEncoder);
		userService.modifyMember(mem);
		return "redirect:/";
	}

	@GetMapping("/product_information_page")
	public String product_information_page() {
		return "user_content/product_information_page";
	}

	@GetMapping("/inquiry_registration")
	public String inquiry_registration() {
		return "user_content/inquiry_registration";
	}

}
