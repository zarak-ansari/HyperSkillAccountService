package account.auth;

import account.entities.LogAction;
import account.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.util.Base64.getEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {


    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private LogService logService;

    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handle auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers("/api/auth/changepass").authenticated()
                .antMatchers("/api/empl/payment").hasAnyRole("ACCOUNTANT", "USER")
                .antMatchers("/api/acct/payments").hasRole("ACCOUNTANT")
                .antMatchers("/api/admin/**").hasRole("ADMINISTRATOR")
                .antMatchers("/api/security/**").hasRole("AUDITOR")
//                .antMatchers("/**").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {


        return (request, response, ex) -> {
            logService.makeLogEntry(LogAction.ACCESS_DENIED, request.getServletPath(), request.getServletPath());
            response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied!");
        };
    }

}
