package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author Eason Lin
 *
 */
@Configuration
@Profile("test")
@EnableWebSecurity
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication()
		.withUser("user").password("password").roles("USER").and()
		.withUser("admin").password("password").roles("USER", "ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
		.antMatchers("/**").hasAuthority("ROLE_USER")
		.and().formLogin();

	}
}
