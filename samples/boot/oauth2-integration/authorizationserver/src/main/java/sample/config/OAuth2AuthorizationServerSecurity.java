package sample.config;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

public class OAuth2AuthorizationServerSecurity extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
				new OAuth2AuthorizationServerConfigurer<>();

		//http.requestMatcher(new OrRequestMatcher(MyAllowedEndpoints())); // will only pass these endpoints
		//http.requestMatcher(new OrRequestMatcher(authorizationServerConfigurer.getEndpointMatchers())); // will only pass these endpoints

//		http.oauth2Login(oauth2 -> oauth2.loginPage("/login")
//				);

		http.authorizeRequests(authorizeRequests ->
				authorizeRequests.antMatchers("/h2-console/**").permitAll()
						.antMatchers("/js/**","/css/**","/images/**","/fonts/**","/vendor/**").permitAll()
						.anyRequest().authenticated()
		).apply(authorizationServerConfigurer);

		http.formLogin(form -> form
				.loginPage("/login").permitAll()
		);
		http.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))); // allow all logout Methods (GET , POST , ...)
		http.csrf(csrf -> csrf.ignoringRequestMatchers(tokenEndpointMatcher(),
				new AntPathRequestMatcher("/h2-console/**", HttpMethod.POST.name())));
		http.headers().frameOptions().disable(); // for h2 console
	}
	// @formatter:on

	private static RequestMatcher tokenEndpointMatcher() {
		return new AntPathRequestMatcher(
				OAuth2TokenEndpointFilter.DEFAULT_TOKEN_ENDPOINT_URI,
				HttpMethod.POST.name());
	}

	private List<RequestMatcher> MyAllowedEndpoints(){
		return Arrays.asList(
				new AntPathRequestMatcher("/login"),
				new AntPathRequestMatcher("/h2-console/**"),
				new AntPathRequestMatcher("/js/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/css/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/images/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/fonts/**",HttpMethod.GET.name()),
				new AntPathRequestMatcher("/vendor/**")
		);
	}


}
