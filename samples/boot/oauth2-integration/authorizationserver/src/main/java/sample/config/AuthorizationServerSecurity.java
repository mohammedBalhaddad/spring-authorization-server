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
import java.util.List;

public class AuthorizationServerSecurity extends OAuth2AuthorizationServerSecurity {

	private OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();
		http.requestMatcher(
				new OrRequestMatcher(getEndpointMatchers())); // will only pass these endpoints

		http.authorizeRequests(authorizeRequests ->
				authorizeRequests.antMatchers("/h2-console/**").permitAll()
						.antMatchers("/js/**","/css/**","/images/**","/fonts/**","/vendor/**").permitAll()
						.anyRequest().authenticated()
		).apply(authorizationServerConfigurer);

		http.formLogin(form -> form.loginPage("/login").permitAll());
		http.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))); // allow all logout Methods (GET , POST , ...)
		http.csrf(csrf -> csrf.ignoringRequestMatchers(tokenEndpointMatcher(),
				new AntPathRequestMatcher("/h2-console/**", HttpMethod.POST.name())));
		http.headers().frameOptions().disable(); // for h2 console
	}


	private static RequestMatcher tokenEndpointMatcher() {
		return new AntPathRequestMatcher(
				OAuth2TokenEndpointFilter.DEFAULT_TOKEN_ENDPOINT_URI,
				HttpMethod.POST.name());
	}

	private List<RequestMatcher> getEndpointMatchers(){
		List<RequestMatcher> list1 =  Arrays.asList(
				new AntPathRequestMatcher("/login"),
				new AntPathRequestMatcher("/h2-console/**"),
				new AntPathRequestMatcher("/js/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/css/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/images/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/fonts/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/vendor/**"));

		List<RequestMatcher> list2 = authorizationServerConfigurer.getEndpointMatchers();

		ArrayList<RequestMatcher> arr = new ArrayList<>();

		arr.addAll(list1);
		arr.addAll(list2);


		System.out.println(list1);
		System.out.println(list2);
		System.out.println(arr);


		return arr ;
	}


}
