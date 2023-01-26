package com.project.project_boot.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.project.project_boot.dto.BasketDTO;
import com.project.project_boot.dto.ProductDTO;
import com.project.project_boot.entity.Content;
import com.project.project_boot.entity.KakaoResult;
import com.project.project_boot.entity.Product;
import com.project.project_boot.service.CommentService;
import com.project.project_boot.service.KakaoService;
import com.project.project_boot.service.ProductService;
import com.project.project_boot.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/Product")
@Controller
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final UserService userService;
	private final KakaoService kakaoService;

	private final CommentService commentService;

	@Value("${org.zerock.upload.path}")
	private String uploadPath;

	@RequestMapping("/Product_List")
	public ModelAndView shoping_main(@Valid ProductDTO productDto,@RequestParam("search_bar")String search_name,
			Principal principal,@PageableDefault(size=12 , page=0)Pageable pageable,Model model
			) {
		Page<Product> pList = productService.productList(search_name,pageable);
		int endPage=0;
		ModelAndView mv = new ModelAndView();
		if(principal != null)
		{
			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name",name);
		}
		int nowPage = pList.getPageable().getPageNumber() + 1;
		if(nowPage==1) {
			endPage = Math.min(nowPage+5, pList.getTotalPages());
		}
		else {
			endPage = Math.min(nowPage+3, pList.getTotalPages());
		}
		int startPage =  Math.max(nowPage - 2, 1);
		
		
		model.addAttribute("paList", pList);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
		model.addAttribute("search_bar",search_name);
		mv.addObject("productList",pList);
		
		// plist.addAll(plist);
		//model.addAttribute("productList", plist);
		mv.setViewName("/bodies/Product_List");
		return mv;
	}
	@RequestMapping("/findfilter")
	public String filter_form(Principal principal,@PageableDefault(size=12 , page=0)Pageable pageable,@RequestParam("select")String select,
			@RequestParam("search_bar")String search,Model model) {
		if (principal != null) {

			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
		}
		int num=Integer.parseInt(select);
		System.out.println("받아 온 것"+ search);
		Page<Product> pList= productService.productList_filter(num,search,pageable);
		int endPage=0;
		System.out.println(pList);
		int nowPage = pList.getPageable().getPageNumber() + 1;
		if(nowPage==1) {
			endPage = Math.min(nowPage+5, pList.getTotalPages());
		}
		else {
			endPage = Math.min(nowPage+3, pList.getTotalPages());
		}
		int startPage =  Math.max(nowPage - 2, 1);
        model.addAttribute("nowPage_main",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("productList", pList);
        model.addAttribute("fliterpage", num);
        model.addAttribute("search_bar", search);
		return "/bodies/Product_List";
	}

	@RequestMapping("/findmainmenu")
	public ModelAndView shopping_main(@Valid ProductDTO productDto, 
			@RequestParam("mainmenu") String mainmenu,@PageableDefault(size=12 , page=0)Pageable pageable,
			Model model,Principal principal) {
		System.out.println("유저가 클릭한 메뉴는 = " + mainmenu);
		Page<Product> pList= productService.productList_main(mainmenu,pageable);
		int endPage=0;
		ModelAndView mv = new ModelAndView();  

		for (Product product2 : pList) {
			if (product2.getSubmenu() == null) {
				break;
			} else {
				System.out.println("랜더링" + product2);
			}

		}
		if (principal != null) {

			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
		}
		int nowPage = pList.getPageable().getPageNumber() + 1;
		if(nowPage==1) {
			endPage = Math.min(nowPage+5, pList.getTotalPages());
		}
		else {
			endPage = Math.min(nowPage+3, pList.getTotalPages());
		}
		int startPage =  Math.max(nowPage - 2, 1);
		
		model.addAttribute("paList", pList);
        model.addAttribute("nowPage_main",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("mainmenu", mainmenu);
		mv.addObject("productList", pList);
		mv.setViewName("/bodies/Product_List");
		
		return mv;
	}

	@RequestMapping("/findsubmenu")
	public ModelAndView shopping_sub(@Valid ProductDTO productDto, @RequestParam("submenu") String submenu,Model model
			,@PageableDefault(size=12 , page=0)Pageable pageable,Principal principal) {

		System.out.println("유저가 클릭한 서브메뉴는 = " + submenu);
		// List<Product>pList=new ArrayList<>();
		Page<Product> pList = productService.productList_sub(submenu,pageable);
		int endPage=0;
		ModelAndView mv = new ModelAndView();

		for (Product product2 : pList) {
			if (product2.getSubmenu() == null) {
				break;
			} else {
				System.out.println("랜더링" + product2);
			}

		}
		if (principal != null) {

			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
		}
		int nowPage = pList.getPageable().getPageNumber() + 1;
		if(nowPage==1) {
			endPage = Math.min(nowPage+5, pList.getTotalPages());
		}
		else {
			endPage = Math.min(nowPage+3, pList.getTotalPages());
		}
		int startPage =  Math.max(nowPage - 3, 1);
		
		model.addAttribute("paList", pList);
        model.addAttribute("nowPage_main",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("submenu", submenu);
		mv.addObject("productList", pList);
		// plist.addAll(plist);
		// model.addAttribute("productList", plist);
		mv.setViewName("/bodies/Product_List");
		return mv;
	}

	@GetMapping("/product_reg_form")
	public String product_reg_form(Principal principal, Model model) {

		model.addAttribute("userid", principal.getName());

		return "/open_win/product_reg";
	}

	@PostMapping("/product_reg")
	public String product_reg(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "price", required = false) int price,
			@RequestParam(value = "EA", required = false) int EA,
			@RequestParam(value = "mainmenu", required = false) String mainmenu,
			@RequestParam(value = "submenu", required = false) String submenu,
			@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "imgfile", required = false) MultipartFile imgfile, ProductDTO productDTO,
			Model model) throws IOException {

		String uuid = UUID.randomUUID().toString();

		String originalname = imgfile.getOriginalFilename();

		String filename = uuid + "_" + originalname.substring(originalname.lastIndexOf("\\") + 1);

		String savename = uploadPath + File.separator + filename;

		productDTO = ProductDTO.MakeDto(title, price, EA, mainmenu, submenu, savename, userid);

		Path savePath = Paths.get(savename);

		System.out.println(savePath);

		try {

			imgfile.transferTo(savePath);
			Product product = Product.MakeProductinfo(productDTO);
			productService.product_registry(product, filename);
			model.addAttribute("successMessage", "상품등록에 성공하셨습니다.");
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", "상품등록실패");
			System.out.println(e.getMessage());
			return "/open_win/product_reg";
		}
		return "redirect:/";
	}


	@RequestMapping("/product_information")
	public String product_information(@RequestParam("title") String title, @RequestParam("price") int price,
			@RequestParam("EA") int EA, @RequestParam("id") String id, Model model,
			@RequestParam("mainmenu") String mainmenu, @RequestParam("img") String img,
			Principal principal) {
		String user = "";
		if (principal == null) {
			model.addAttribute("guest","guest");
            model.addAttribute("username","guest");
            model.addAttribute("payplz","회원가입이나 로그인 후에 진행해주세요!");
		} else if (principal != null) {
			model.addAttribute("username",principal.getName());
			user=principal.getName();
			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name",name);
			List<KakaoResult>kList=kakaoService.finduser(principal.getName(), img);
			if(kList.isEmpty()==true) {
				model.addAttribute("payplz","구매 후에 작성이 가능합니다!");
			}
		}

		
		//장바구니
				BasketDTO product_info =new BasketDTO();
				product_info =BasketDTO.createBasketDTO(id, title,price,mainmenu ,img ,EA,user);
				model.addAttribute("plist",product_info);
				//댓글 보기
				List<Content>clist=commentService.viewComment(img);
				int staradd = 0;
				for (Content content : clist) {
					 staradd =content.getRating()+staradd;
					 //별점의 총합
				}
				
				
				float stars_normal = (float)staradd/(float)clist.size();
				System.out.println(stars_normal); 
				if(stars_normal>=4.5) {
					model.addAttribute("summary", "구매자들의 만족도가 매우 높습니다.");
					
				}
				else if(stars_normal<=4.49&&stars_normal>=4) {
					model.addAttribute("summary", "구매자들의 만족도가 높습니다.");
				}
				else if(stars_normal<=3.99&&stars_normal>=3) {
					model.addAttribute("summary", "구매자들의 만족도가 보통입니다.");
				}
				else if(stars_normal<=2.99) {
					model.addAttribute("summary", "구매자들이 추천하지 않아요.");
				}
				if(Float.isNaN(stars_normal)) {
					model.addAttribute("nocomment", "구매자들의 후기가 없는 제품이에요.");
				};
				model.addAttribute("comment",clist);
				model.addAttribute("star_avs", stars_normal);
				return "/user_content/product_information_page";
	}
	//구매자 댓글
		@ResponseBody
		@RequestMapping("/pr_comment")
		public Content product_comment(
				@RequestParam("coment_img")String img,
				@RequestParam("user_coment")String answer,Principal principal,
				@RequestParam("submit_star")String rating,
				Model model)
		{
			int rate = Integer.parseInt(rating);
			
			Date date = new Date();
			Content content = new Content(); 
				content = content.makecontent(principal.getName(),answer,img,date,rate);
				System.out.println("내가 준 별점"+rate);
				commentService.savingcomment(content);
				

			return content;
		}

}
