package com.project.project_boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.project.project_boot.seq_log.CustomLoginSuccessHandler;
import com.project.project_boot.service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
//       http.exceptionHandling()
//        .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    			
       http
    	.authorizeRequests()
    	.antMatchers("/", "/User/sing_up","/User/login_form","/Payment").permitAll();
       
       
    	http
    .formLogin()
    	.loginPage("/User/login_form")
    	.loginProcessingUrl("/User/login_form")
    	.usernameParameter("id")
		.passwordParameter("pw")
		.defaultSuccessUrl("/")
		.successHandler(successHandler())
		.failureUrl("/User/login/error")
    	.and().csrf().disable();
    	
    	
    	http.logout()
    	.logoutUrl("/logout_form")
    	.logoutSuccessUrl("/");
    	
    	
    	
    	return http.build();
    	
    }
    
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationSuccessHandler successHandler() {
    	return new CustomLoginSuccessHandler("/");//default로 이동할 url
    }
    
}

	