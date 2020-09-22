package sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
public class OAuth2AuthorizationServerConfiguration {

	@Bean
	public WebSecurityConfigurer<WebSecurity> defaultOAuth2AuthorizationServerSecurity() {
		return new OAuth2AuthorizationServerSecurity();
	}

}
