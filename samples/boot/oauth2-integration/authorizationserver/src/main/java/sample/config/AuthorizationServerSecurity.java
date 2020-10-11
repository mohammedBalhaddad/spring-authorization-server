package sample.config;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthorizationServerSecurity extends OAuth2AuthorizationServerSecurity {



	private static final String H2_ANT_PATH = "/h2-console/**" ;
	private static final String JS_ANT_PATH = "/js/**" ;
	private static final String CSS_ANT_PATH = "/css/**" ;
	private static final String IMG_ANT_PATH = "/images/**" ;
	private static final String FONT_ANT_PATH = "/fonts/**" ;
	private static final String VNDR_ANT_PATH = "/vendor/**" ;

	private static final String LOGIN_PATH = "/login" ;
	private static final String LOGOUT_PATH = "/logout" ;

	private OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

		http
				.requestMatcher( // will only pass these endpoints
						new OrRequestMatcher( getAllEndpointsMatchers() )
				)
				.authorizeRequests(authorizeRequests -> authorizeRequests
						.antMatchers(H2_ANT_PATH , JS_ANT_PATH , CSS_ANT_PATH ,
								IMG_ANT_PATH , FONT_ANT_PATH , VNDR_ANT_PATH , LOGIN_PATH ).permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage(LOGIN_PATH)
				)
				.logout(logout -> logout
						.logoutRequestMatcher( new AntPathRequestMatcher(LOGOUT_PATH) ) // allow all logout Methods (GET , POST , ...)
				)
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(
								getCsrfTokenEndpointMatcher()
						)
				)
				.headers(headers -> headers
						.frameOptions().disable() // Temp : for h2 console
				)
				.apply(authorizationServerConfigurer);

	}


	private RequestMatcher [] getCsrfTokenEndpointMatcher() {
		return Stream.concat(
			authorizationServerConfigurer.getEndpointMatchers().stream(),
			Stream.of(new AntPathRequestMatcher(H2_ANT_PATH, HttpMethod.POST.name()))
		).toArray(RequestMatcher[]::new);
	}

	private List<RequestMatcher> getAllEndpointsMatchers(){

		List<RequestMatcher> loginEndpoints = Arrays.asList(
				new AntPathRequestMatcher(LOGIN_PATH),
				new AntPathRequestMatcher(H2_ANT_PATH), // Temp : for h2 console
				new AntPathRequestMatcher(JS_ANT_PATH,HttpMethod.GET.name()),
				new AntPathRequestMatcher(CSS_ANT_PATH,HttpMethod.GET.name()),
				new AntPathRequestMatcher(IMG_ANT_PATH,HttpMethod.GET.name()),
				new AntPathRequestMatcher(FONT_ANT_PATH,HttpMethod.GET.name()),
				new AntPathRequestMatcher(VNDR_ANT_PATH));

		return Stream.concat(
				loginEndpoints.stream(),
				authorizationServerConfigurer.getEndpointMatchers().stream()
		).collect(Collectors.toList());
	}


}
