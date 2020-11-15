package com.kil.tutor.config;

import com.kil.tutor.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ANY_POSTFIX = "/**";

    private final UserDetailsService userDetailsService;
    private final JwtAuthFilter authFilter;

    @Autowired
    public SecurityConfig(
            @Qualifier("appUserDetailsService") UserDetailsService userDetailsService,
            JwtAuthFilter authFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.authFilter = authFilter;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.cors().and().csrf().disable();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .authorizeRequests()
//                .antMatchers("/v2/api-docs",
//                        "/configuration/ui",
//                        "/swagger-resources" + ANY_POSTFIX,
//                        "/configuration/security",
//                        "/swagger-ui.html",
//                        "/static/web-socket.html",
//                        "/webjars" + ANY_POSTFIX)
//                .permitAll()

                .antMatchers(ANY_POSTFIX).permitAll()//TODO remove after cors fix
                .anyRequest()
                .authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
