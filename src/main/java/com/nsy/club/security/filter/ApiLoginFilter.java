package com.nsy.club.security.filter;

import com.nsy.club.security.dto.ClubAuthMemberDTO;
import com.nsy.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    //jjwt를 이용한 jwt 생성 및 검증
    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessUrl, JWTUtil jwtUtil){
        super(defaultFilterProcessUrl);
        this.jwtUtil = jwtUtil;

    }

    /**
     *  AuthenticationManger를 이용해서 인증처리 하기
     *  -> UsernamePasswordAuthenticationToken을 사용하여 인증
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("------------------------ApiLoginFilter----------------------------");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pw);


//        if(email == null){
//            throw new BadCredentialsException("email cannot be null");
//        }

        return getAuthenticationManager().authenticate(authToken);
    }

    /**
     * 로그인 성공 처리
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        log.info("-----------------ApiLoginFilter---------------------");
        log.info("successfulAuthentication : " + authResult);

        log.info(authResult.getPrincipal());    //현재 로그인(인증)한 사용자 정보

        String email = ((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();
        String token = null;

        try{

            token = jwtUtil.generateToken(email);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
