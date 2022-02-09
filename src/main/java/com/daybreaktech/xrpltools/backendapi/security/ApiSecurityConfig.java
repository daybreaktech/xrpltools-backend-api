package com.daybreaktech.xrpltools.backendapi.security;

import com.daybreaktech.xrpltools.backendapi.security.jwt.AppEntryPoint;
import com.daybreaktech.xrpltools.backendapi.security.jwt.AppTokenFilter;
import com.daybreaktech.xrpltools.backendapi.service.XrplUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private XrplUserDetailsService xrplUserDetailsService;

    @Autowired
    private AppEntryPoint appEntryPoint;

    @Value("${v1API}")
    private String apiContext;

    @Bean
    public AppTokenFilter authenticationJwtTokenFilter() {
        return new AppTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(xrplUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(appEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            //Available to not authenticated or public
            .authorizeRequests().antMatchers(apiContext + "/auth/**").anonymous().and()
            .authorizeRequests().antMatchers(apiContext + "/airdrop/publisher/**").anonymous().and()
            .authorizeRequests().antMatchers(apiContext + "/notifications/**").anonymous().and()
            .authorizeRequests().antMatchers(apiContext + "/misc/**").anonymous().and()

            //AIRDROP and TRUSTLINE Admins
            .authorizeRequests().antMatchers(apiContext + "/airdrop/**").hasAnyAuthority("SUPER_ADMIN", "AIRDROP_ADMIN").and()
            .authorizeRequests().antMatchers(apiContext + "/trustlines/**").hasAnyAuthority("SUPER_ADMIN", "AIRDROP_ADMIN", "TRUSTLINE_ADMIN").and()

            // Only for Super admins
            .authorizeRequests().antMatchers(apiContext + "/users/**").hasAnyAuthority("SUPER_ADMIN")
            .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
