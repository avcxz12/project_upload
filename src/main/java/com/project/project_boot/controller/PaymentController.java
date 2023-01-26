package com.project.project_boot.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.project.project_boot.dto.BasketDTO;
import com.project.project_boot.dto.ProductDTO;
import com.project.project_boot.entity.Basket;
import com.project.project_boot.entity.KakaoResult;
import com.project.project_boot.entity.Product;
import com.project.project_boot.service.BasketService;
import com.project.project_boot.service.KakaoService;
import com.project.project_boot.service.ProductService;
import com.project.project_boot.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/Payment")
@Controller
@RequiredArgsConstructor
public class PaymentController {

	private final BasketService basketService;
	private final UserService userService;
	private final KakaoService kakaoService;
	private final ProductService productService;

	@GetMapping("/basket_form")
	public String modify_form(Principal principal, Model model, String buyer) {
		if (principal == null) {
			return "user_content/login_form";
		} else if (principal != null) {
			buyer = principal.getName();
			model.addAttribute("username", buyer);
			String name = userService.findingUserName(principal.getName());
			model.addAttribute("name", name);
			List<Basket> bList = basketService.BasketList(buyer);
			int total_amount = 0;
			int products_count = 0;
			if(bList.size() == 0)
			{
				model.addAttribute("NullMessage","장바구니가 비었습니다.");
				return "user_content/basket_form";
			}
			String title =bList.get(0).getTitle();
			
			
			
			for (Basket basket : bList) {
				total_amount = total_amount + basket.getPrice();
				products_count = products_count+1;
			}
			if(products_count !=1)
			{
				title = title +"외"+(products_count-1)+"개";
			}
			
			model.addAttribute("counts",products_count);
			model.addAttribute("titles",title);
			model.addAttribute("total_amount", total_amount);
			model.addAttribute("productList", bList);
			model.addAttribute("firstimg",bList.get(0).getImg());
		}

		return "user_content/basket_form";
	}

	@PostMapping("/basket_remove")
	public String bascket_delete(@RequestParam("title") String title, @RequestParam("buyer") String buyer,Model model)

	{
		basketService.BasketdeleteTest(title, buyer);
		List<Basket> bList = basketService.BasketList(buyer);
		model.addAttribute("productList", bList);
		return "user_content/basket_form";
	}

	@ResponseBody
	@PostMapping("/savebasket")
	public void savebascket(@RequestParam("id") String userid, @RequestParam("mainmenu") String mainmenu,
			@RequestParam("title") String title, @RequestParam("price") int price, @RequestParam("img") String img,
			@RequestParam("count") int count, @RequestParam("username") String buyer, Model model, Principal principal)

	{
//		String user="";
		Basket basket = new Basket();
//		if(principal != null){
//			model.addAttribute("username",principal.getName());
//			user=principal.getName();
//			System.out.println("로그인 아이디:"+user);
//		}
		int plusprice = price * count;
		List<Basket> bList = basketService.BasketTest(title, buyer);
		if (bList != null) {
			basketService.BasketdeleteTest(title, buyer);

		}
		basket = Basket.createBasket(BasketDTO.createBasketDTO(userid, title, plusprice, mainmenu, img, count, buyer));
		basketService.BasketImport(basket);
	}

	@RequestMapping("/kakaopay_detail.cls")
	public String kakaopay_detail(@RequestParam("tid") String tid,@RequestParam("price")
	String price,@RequestParam("title")String title,@RequestParam("img")String img,@RequestParam("daytime")String daytime,Principal principal,Model model) throws ParseException {
		try {
			URL address = new URL("https://kapi.kakao.com/v1/payment/order");

			HttpURLConnection payconnect = (HttpURLConnection) address.openConnection();
			// url 을 사용하려면 trycatch가 필요하다
			payconnect.setRequestMethod("POST");
			// kakao 에서 포스트형식으로 넘겨달라고햇다
			payconnect.setRequestProperty("Authorization", "KakaoAK f719721ea68e4ce56d5c70c49ce9da18");
			// 카카오에서 발급해준 어드민키 를 authorization 형식으로 넘겨달라고 카카오에서 시켯다
			payconnect.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			// 컨텍트 타입도 카카오에서 시키는대로 했다
			payconnect.setDoInput(true);
			payconnect.setDoOutput(true);
			System.out.println(tid);
			// 요기까지 기본설정
			String parameter = "cid=TC0ONETIME&tid=" + tid;
			// 카카오에서 ox 있는 길게 나와있는표에서 필요한것들만 넣었다

			OutputStream DataPakage = payconnect.getOutputStream();
			// outputstream은 서버에서 주고싶은 데이터를 담는다.

			DataOutputStream throwData = new DataOutputStream(DataPakage);
			// 아웃풋 스트림에 담긴 데이터를 카카오로 던져주는아이
			throwData.writeBytes(parameter);
			// 카카오에서 바이트형식으로 달라했으니 바이트형식으로 형변환을 해준다.

			throwData.flush();
			// 본인한테 담겨있는 데이터를 카카오로 넘김과 동시에 본인을 비운다.
			throwData.close();
			// 이제 더이상 데이터 보낼게 없음으로 닫는다 flush 없이 해당 문구만써도 자동으로 flush 해준다.

			int resultset = payconnect.getResponseCode();
			// 결과를 이제 받아온다.
			InputStream resultData;
			// 결과를 받아온놈을 이제 자바에서 읽을수있도록 바꿔주는애? 인거같다.
			System.out.println("결과 코드값은=" + resultset);
			if (resultset == 200) // 성공코드가 200이라고한다. 나머지는 전부 fail이라고한다.
			{
				resultData = payconnect.getInputStream();
			} else {
				resultData = payconnect.getErrorStream();
			}
			InputStreamReader resultReader = new InputStreamReader(resultData);
			// 자바에서 문자열로 읽을수잇게 형변환해준다

			@SuppressWarnings("deprecation")
			JSONParser json = new JSONParser();
			JSONObject obj = (JSONObject) json.parse(resultReader);
			System.out.println(obj);
			model.addAttribute("tid",tid);
			model.addAttribute("price",price);
			model.addAttribute("title",title);
			model.addAttribute("img",img);
			model.addAttribute("daytime",daytime);
			model.addAttribute(principal.getName());
			return "/open_win/kakao_detail";
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"NO\"}";
	}

	@RequestMapping("/kakaopay.cls")
	@ResponseBody
	public Object kakaopay_for_once(Principal principal, @RequestParam("title") String title,
			@RequestParam("price") int price,@RequestParam("img") String img
			,@RequestParam("count") int count,HttpSession session)
			throws ParseException {
		
		try {
			URL address = new URL("https://kapi.kakao.com/v1/payment/ready");

			HttpURLConnection payconnect = (HttpURLConnection) address.openConnection();
			payconnect.setRequestMethod("POST");
			payconnect.setRequestProperty("Authorization", "KakaoAK f719721ea68e4ce56d5c70c49ce9da18");
			payconnect.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			payconnect.setDoOutput(true);
			payconnect.setDoInput(true);

			String item_name = URLEncoder.encode(title, "UTF-8");
			int t_price=price*count;
			System.out.println("얼마임?"+t_price);

			String parameter = "cid=TC0ONETIME&partner_order_id=test18722342&" + "partner_user_id="
					+ principal.getName() + "&" + "item_name=" + item_name + "&"// 수정
					+ "quantity=1&total_amount=" + t_price + "&"// 최종결제 수정
					+ "vat_amount=0&tax_free_amount=0&" 
					+ "approval_url=http://localhost:8083/Payment/kakaoapporve.cls&" // approval_url=https://localhost:8080/success
					// 성공
					+ "fail_url=http://localhost:8083/&"
					// fail_url=https://localhost:8080/fail 실패
					+ "cancel_url=http://localhost:8083/"; // cancel_url=https://localhost:8080/cancel
			// 카카오에서 ox 있는 길게 나와있는표에서 필요한것들만 넣었다

			OutputStream DataPakage = payconnect.getOutputStream();
			// outputstream은 서버에서 주고싶은 데이터를 담는다.

			DataOutputStream throwData = new DataOutputStream(DataPakage);
			// 아웃풋 스트림에 담긴 데이터를 카카오로 던져주는아이
			throwData.writeBytes(parameter);
			// 카카오에서 바이트형식으로 달라했으니 바이트형식으로 형변환을 해준다.

			throwData.flush();
			// 본인한테 담겨있는 데이터를 카카오로 넘김과 동시에 본인을 비운다.
			throwData.close();
			// 이제 더이상 데이터 보낼게 없음으로 닫는다 flush 없이 해당 문구만써도 자동으로 flush 해준다.

			int resultset = payconnect.getResponseCode();
			// 결과를 이제 받아온다.
			System.out.println(resultset);
			InputStream resultData;
			// 결과를 받아온놈을 이제 자바에서 읽을수있도록 바꿔주는애? 인거같다.

			if (resultset == 200) // 성공코드가 200이라고한다. 나머지는 전부 fail이라고한다.
			{
				System.out.println("결제요청완료하였습니다. 결제 승인 진행해야합니다.");
				resultData = payconnect.getInputStream();

			} else {
				resultData = payconnect.getErrorStream();
			}
			InputStreamReader resultReader = new InputStreamReader(resultData);
			// 자바에서 문자열로 읽을수잇게 형변환해준다

			@SuppressWarnings("deprecation")
			JSONParser json = new JSONParser();
			JSONObject obj = (JSONObject) json.parse(resultReader);
			System.out.println(title);
			Date date = new Date();
			String tid = (String) obj.get("tid");
			System.out.println(tid);
			
			session.setAttribute("title", title);
			session.setAttribute("tid",tid);
			session.setAttribute("price",price);
			session.setAttribute("count",count);
			session.setAttribute("img",img);
			return obj;

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"NO\"}";
	}

	@RequestMapping("/kakaoapporve.cls")
	public String kakaopay_apporve(Principal principal, @RequestParam("pg_token") String token,
			HttpSession session) throws ParseException {
		
		
		try {
			URL address = new URL("https://kapi.kakao.com/v1/payment/approve");

			HttpURLConnection payconnect = (HttpURLConnection) address.openConnection();
			// url 을 사용하려면 trycatch가 필요하다
			payconnect.setRequestMethod("POST");
			// kakao 에서 포스트형식으로 넘겨달라고햇다
			payconnect.setRequestProperty("Authorization", "KakaoAK f719721ea68e4ce56d5c70c49ce9da18");
			// 카카오에서 발급해준 어드민키 를 authorization 형식으로 넘겨달라고 카카오에서 시켯다
			payconnect.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			// 컨텍트 타입도 카카오에서 시키는대로 했다
			payconnect.setDoOutput(true);
			// 요기까지 기본설정

			String parameter = "cid=TC0ONETIME&tid=" + (String)session.getAttribute("tid") + "&partner_order_id=test18722342&partner_user_id="
					+ principal.getName() + "&pg_token=" + token;

			OutputStream DataPakage = payconnect.getOutputStream();
			// outputstream은 서버에서 주고싶은 데이터를 담는다.

			DataOutputStream throwData = new DataOutputStream(DataPakage);
			// 아웃풋 스트림에 담긴 데이터를 카카오로 던져주는아이
			throwData.writeBytes(parameter);
			// 카카오에서 바이트형식으로 달라했으니 바이트형식으로 형변환을 해준다.

			throwData.flush();
			// 본인한테 담겨있는 데이터를 카카오로 넘김과 동시에 본인을 비운다.
			throwData.close();
			// 이제 더이상 데이터 보낼게 없음으로 닫는다 flush 없이 해당 문구만써도 자동으로 flush 해준다.

			int resultset = payconnect.getResponseCode();
			// 결과를 이제 받아온다.
			System.out.println(resultset);
			InputStream resultData;
			// 결과를 받아온놈을 이제 자바에서 읽을수있도록 바꿔주는애? 인거같다.

			if (resultset == 200) // 성공코드가 200이라고한다. 나머지는 전부 fail이라고한다.
			{
				System.out.println("결제승인 성공입니다.");
				resultData = payconnect.getInputStream();
				

			} else {
				resultData = payconnect.getErrorStream();
			}
			InputStreamReader resultReader = new InputStreamReader(resultData);
			// 자바에서 문자열로 읽을수잇게 형변환해준다

			@SuppressWarnings("deprecation")
			JSONParser json = new JSONParser();
			JSONObject obj = (JSONObject) json.parse(resultReader);
			Date date = new Date();
	            KakaoResult kakaoResult = KakaoResult.makeresult(principal.getName(),
	            		(String)session.getAttribute("title"),(int)session.getAttribute("price")
	            		,(int)session.getAttribute("count"),(String)session.getAttribute("img"),date,
	            		(String)session.getAttribute("tid"));
	           List<Product>pList= productService.findtitleimg((String)session.getAttribute("img"));
	           for (Product p : pList) {
				int totalEA=p.getEA()-(int)session.getAttribute("count");
				ProductDTO productDto=ProductDTO.MakeDto(p.getTitle(),p.getPrice(),totalEA,p.getMainmenu(),p.getSubmenu(),p.getImg(),p.getUserid());
				Product proup=Product.MakeProductinfo(productDto);
				productService.product_easave(proup);
			}
	            
	  	      kakaoService.saveResult(kakaoResult);
	  	    session.removeAttribute("title");
			session.removeAttribute("tid");
			session.removeAttribute("price");
			session.removeAttribute("count");
			session.removeAttribute("img");

			return "redirect:/success";

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/fail";
	}
	
	
	@RequestMapping("/kakaocancel.cls")
	public String kakaopay_cancel(Principal pricipal,@RequestParam("tid") String tid,@RequestParam("price") int price,Model model)throws ParseException
	{
		try
		{
			URL address = new URL("https://kapi.kakao.com/v1/payment/cancel");
			
			HttpURLConnection payconnect = (HttpURLConnection) address.openConnection();
			// url 을 사용하려면 trycatch가 필요하다
			payconnect.setRequestMethod("POST");
			// kakao 에서 포스트형식으로 넘겨달라고햇다
			payconnect.setRequestProperty("Authorization", "KakaoAK f719721ea68e4ce56d5c70c49ce9da18");
			// 카카오에서 발급해준 어드민키 를 authorization 형식으로 넘겨달라고 카카오에서 시켯다
			payconnect.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			// 컨텍트 타입도 카카오에서 시키는대로 했다
			payconnect.setDoOutput(true);
			// 요기까지 기본설정

			String parameter = "cid=TC0ONETIME&tid=" + tid + "&cancel_amount="+price+"&cancel_tax_free_amount=0";

			OutputStream DataPakage = payconnect.getOutputStream();
			// outputstream은 서버에서 주고싶은 데이터를 담는다.

			DataOutputStream throwData = new DataOutputStream(DataPakage);
			// 아웃풋 스트림에 담긴 데이터를 카카오로 던져주는아이
			throwData.writeBytes(parameter);
			// 카카오에서 바이트형식으로 달라했으니 바이트형식으로 형변환을 해준다.

			throwData.flush();
			// 본인한테 담겨있는 데이터를 카카오로 넘김과 동시에 본인을 비운다.
			throwData.close();
			// 이제 더이상 데이터 보낼게 없음으로 닫는다 flush 없이 해당 문구만써도 자동으로 flush 해준다.

			int resultset = payconnect.getResponseCode();
			// 결과를 이제 받아온다.
			System.out.println(resultset);
			InputStream resultData;
		
			
			
			if (resultset == 200) // 성공코드가 200이라고한다. 나머지는 전부 fail이라고한다.
			{
				resultData = payconnect.getInputStream();

			} else {
				resultData = payconnect.getErrorStream();
			}
			InputStreamReader resultReader = new InputStreamReader(resultData);

			@SuppressWarnings("deprecation")
			JSONParser json = new JSONParser();
			JSONObject obj = (JSONObject) json.parse(resultReader);
			
			System.out.println(obj);
			
			kakaoService.paycancel(tid);
			return "redirect:/User/mypage";
		}
		catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return "redirect:/fail";
	}

}