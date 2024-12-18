package team5.onlybuns.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import team5.onlybuns.security.auth.RestAuthenticationEntryPoint;
import team5.onlybuns.security.auth.TokenAuthenticationFilter;
import team5.onlybuns.service.impl.CustomUserDetailsService;
import team5.onlybuns.util.TokenUtils;

@Configuration

@EnableWebSecurity


@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


 	@Bean
 	public DaoAuthenticationProvider authenticationProvider() {
 	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
 	    authProvider.setPasswordEncoder(passwordEncoder());

 	    return authProvider;
 	}



 	@Autowired
 	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


 	@Bean
 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
 	    return authConfig.getAuthenticationManager();
 	}

	@Autowired
	private TokenUtils tokenUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
		http.authorizeRequests().antMatchers("/auth/**").permitAll()
				.antMatchers("/actuator/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/posts").permitAll()
			.antMatchers(HttpMethod.GET, "/api/posts/{postId}/comments").permitAll()
			.antMatchers(HttpMethod.GET, "/uploads/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/posts/{postId}/like_count").permitAll()
			.anyRequest().authenticated().and()
			.cors().and()
			.addFilterBefore(new TokenAuthenticationFilter(tokenUtils,  userDetailsService()), BasicAuthenticationFilter.class);

		http.csrf().disable();
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }


}
