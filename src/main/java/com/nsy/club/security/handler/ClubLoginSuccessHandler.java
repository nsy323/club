package com.nsy.club.security.handler;

import com.nsy.club.security.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 성공 이후 처리 담당
 */

@Log4j2
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {

    //spring security가 화면 이동에 대한 규칙을 정의한 인터페이스 객체 생성
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    //암호화
    private PasswordEncoder passwordEncoder;

    public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        log.info("--------------------------------");
        log.info("onAuthenticationSuccess.........");

        ClubAuthMemberDTO authMember = (ClubAuthMemberDTO) authentication.getPrincipal();

        boolean fromSocial = authMember.isFromSocial();

        log.info("Need Modify Member?" + fromSocial);

        boolean passwordResult = passwordEncoder.matches("1111", authMember.getPassword());

        if(fromSocial && passwordResult){
            redirectStrategy.sendRedirect(request, response,"/member/modify?from=social");
        }
    }
}
