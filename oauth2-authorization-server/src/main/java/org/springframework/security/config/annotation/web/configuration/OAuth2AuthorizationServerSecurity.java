/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.config.annotation.web.configuration;


import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.web.JwkSetEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * {@link WebSecurityConfigurerAdapter} providing default security configuration for OAuth 2.0 Authorization Server.
 *
 * @author Joe Grandja
 * @since 0.0.1
 */
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

		//ttp.requestMatcher(new OrRequestMatcher(authorizationServerConfigurer.getEndpointMatchers())); // will only pass these endpoints
		//http.requestMatcher(new OrRequestMatcher(MyAllowedEndpoints())); // will only pass these endpoints

		http.authorizeRequests(authorizeRequests ->
				authorizeRequests.antMatchers("/h2-console/**").permitAll()
						.antMatchers("/js/**","/css/**","/images/**","/fonts/**","/vendor/**").permitAll()
					.anyRequest().authenticated()
		).apply(authorizationServerConfigurer);

		http.formLogin(form -> form.loginPage("/login").permitAll());
		http.logout();
		http.csrf(csrf -> csrf.ignoringRequestMatchers(tokenEndpointMatcher(),new AntPathRequestMatcher("/h2-console/**",HttpMethod.POST.name())));
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
