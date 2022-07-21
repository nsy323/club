package com.nsy.club.config;

import com.nsy.club.security.filter.ApiCheckFilter;
import com.nsy.club.security.filter.ApiLoginFilter;
import com.nsy.club.security.handler.ApiLoginFailHandler;
import com.nsy.club.security.handler.ClubLoginSuccessHandler;
import com.nsy.club.security.service.ClubUserDetailsService;
import com.nsy.club.security.util.JWTUtil;
import io.jsonwebtoken.Jwt;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * BCryptPasswordEncoder : bcrypt라는 해시함수를 이용해서 패스워드 암호화
 *
 * @EnableGlobalMethodSecurity - prePostEnabled : @PreAuthorize를 이용하기 위한 속성
 *                             - securedEnabled : 예전 버전의 @Secure 사용 가능 여부 지정
 */
@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClubUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * '/sample/all'에는 별도의 로그인 없이도 접근이 가능하도록 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
//              http.authorizeRequests()
//              .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("USER");
        
        http.formLogin();       //인가/인증에 문제시 로그인 화면 이동

        http.csrf().disable();  // csrf 토큰 발행안함 (REST 방식 사용시 CSRF 토큰의 값을 알아내야하는 불편함이 있기 때문에 발행하지 않는 경우도 있음)

        http.logout();          //로그아웃(CSRF토큰 사용시 반드시 POST방식으로만 로그아웃 처리해야 함)

        http.oauth2Login().successHandler(successHandler());     //실제 로그인시 OAuth를 사용한 로그인이 가능하도록 설정

        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);  //7일간 쿠키 유지

        //ApiCheckFilter를 UsernamePasswordAuthenticationFilter이전에 동작하도록 지정
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        //apiLoginFilter를 UsernamePasswordAutenticationFilter이전에 동작하도록 지정
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 로그인 성공 핸들러 생성
     * @return
     */
    public ClubLoginSuccessHandler successHandler(){
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    /**
     * ApicheckFilter Bean으로 등록
     *
     * 프로젝트 실행시 /notes/로 시작하는 경로에만 로드 출력
     *
     * @return
     */
    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/notes/**/*", jwtUtil());
    }

    /**
     * ApiLoginFilter Bean으로 등록
     * @return
     * @throws Exception
     */
    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{

        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login",jwtUtil());

        apiLoginFilter.setAuthenticationManager(authenticationManager());   //AuthenticationManager를 이용한 인증처리

        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());  //로그인 실패 처리 핸들러

        return apiLoginFilter;
    }

    /**
     * jwtUtil Bean으로 등록
     *
     * @return
     */
    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }

    /**@
     *  보안인증(Athentication)설정
     *
     * @param auth : 코드를 통해 직접 인증 매니저 설정 사용 가능
     * @throws Exception

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{


        auth.inMemoryAuthentication()
                .withUser("user1") //사용자 계정 user1
                .password("$2a$10$FNkN52/0Z.WMMU2YoJ5H6ufa2jZyZqt/IhJhbGm9IHkCxAWB0ze1m")   //1111 패스워드 인코딩 결과
                .roles("USER");
    }
     */
}
