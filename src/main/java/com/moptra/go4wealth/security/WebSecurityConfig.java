package com.moptra.go4wealth.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.moptra.go4wealth.configuration.JwtConfiguration;
import com.moptra.go4wealth.repository.RoleRepository;
import com.moptra.go4wealth.repository.UserRepository;
import com.moptra.go4wealth.repository.UserRoleRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Autowired
	JwtConfiguration jwtConfiguration;

	@Autowired
	PasswordEncoder passwordEncoder;

    @Autowired
	UserDetailService userDetailService;
    
    @Autowired
	UserRepository userRepository;
    
    @Autowired
	UserRoleRepository userRoleRepository;
    
    @Autowired
	RoleRepository roleRepository;

	protected void configure(HttpSecurity http) throws Exception {
		http
		.antMatcher("/**")
		.csrf().disable()
		.authorizeRequests().antMatchers(
				"/secured/uma/test",
				"/secured/uma/signupByFacebook", 
				"/secured/uma/signupByGoogle", 
				"/secured/uma/isUserExist/**",
				"/secured/uma/editUser", 
				"/secured/uma/getAllUsers",
				"/secured/uma/verify/otp/mobileNumber",
				"/secured/uma/signupByMobile",
				"/secured/uma/signupByGuestUser",
				"/secured/uma/enquiry",
				"/secured/uma/agent/register",
				"/secured/uma/editAgent",
				"/secured/uma/forgetPassword/**",
				"/secured/uma/forgetPassword/verify/otp",
				"/secured/uma/resetPassword",
				"/secured/uma/changePassword",
				"/sip/api/**",
				"/sip/advance/**",			
				"/sip/**",
				"/secured/uma/resendOtp/**",
				"/secured/uma/verifyToken",
				"/secured/uma/verify-email",
				"/e-mandate/image/*/*",
				"/prs/api/**",
				"/prs/fund-scheme/api/upload/scheme-master",
				"/prs/fund-scheme/api/getAllScheme/**",
				"/prs/fund-scheme/api/getAllTopScheme",
				"/secured/uma/getUserBlogList",
				"/admin/getImageById/**",
				"/admin/contactUsFormData",
				"/admin/getUserTestimonialData",
				"/admin/getTestimonialImageById/**",
				"/secured/uma/getUserBlogByCategory/**",
				"/secured/uma/getUserBlogCategory",
				"/secured/uma/getUserBlogById/**",
				"/secured/uma/getUserRelatedBlog/**",
				"/admin/getSeoInfo/**",
				"/admin/getHomeVideoUrl/**",
				"/admin/showHomeVideo",
				"/admin/api/export/excel/download",
				"/admin/api/export/excel/download/confirmedOrders",
				"/admin/api/export/excel/download/purchasedOrders",
				"/admin/api/export/excel/download/canceledOrders",
				"/admin/api/export/excel/download/onboardingCompletedUsers",
				"/admin/api/export/excel/download/onboardingNotCompletedUsers",
				"/admin/api/export/excel/download/kycCompletedUsers",
				"/admin/api/export/excel/download/kycNotCompletedUsers",
				"/admin/api/export/excel/download/todayRegisteredUsers",
				"/admin/api/export/excel/download/todayConfirmedOrder",
				"/admin/api/export/excel/download/todayCanceledOrder",
				"/admin/api/export/excel/download/todayPurchasedOrder",
				"/admin/api/export/excel/download/todayOnboardingCompletedUser",
				"/admin/api/export/excel/download/todayKycCompletedUser",
				"/admin/api/export/excel/download/pendingOrders",
				"/prs/fund-scheme/api/calculateSipNavData",
				"/prs/fund-scheme/api/allSchemeType",
				"/prs/fund-scheme/api/searchScheme",
				"/prs/fund-scheme/api/getSchemeDetails/**",
				"/prs/fund-scheme/api/getSchemeDetailsByKeyword/**",
				"/admin/getTotalPagesForPagination/**",
				"/admin/getTotalPagesForSchemePagination/**",
                "/prs/fund-scheme/api/upload/saveExcelDataToDbUsingBase64",
				"/prs/doPayment/**",
				"/prs/modelportfolio/getModelportfolioList",
				"/prs/modelportfolio/getModelportfolioDetailByCategory/**",
				"/prs/fund-scheme/upload/updateNav/updateSchemesNavViaAutoDownload",
				"/admin/getSchemeKeywordForSeo",
				"/prs/fund-scheme/api/getAllRecommendedScheme",
				"/prs/fund-scheme/api/updateBillerStatus",
				"/prs/fund-scheme/api/getBillerStatus",
				"/prs/fund-scheme/getNavFromAmfi",
				"/prs/fund-scheme/test"
				
				).permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilterBefore(authenticationFilter(),UsernamePasswordAuthenticationFilter.class)
		.addFilter(new JwtUserProfileAuthorizationFilter(jwtConfiguration,userRepository,userDetailService))
		// Disable session creation on Spring Security.
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	protected JwtUserProfileAuthenticationFilter authenticationFilter() throws Exception {
		JwtUserProfileAuthenticationFilter authenticationFilter = new JwtUserProfileAuthenticationFilter(authenticationManager(), jwtConfiguration,userRepository,userDetailService);
		if(authenticationFilter != null)
		authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
		return authenticationFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoderImpl();
	}
 
}