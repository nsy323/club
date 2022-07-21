package com.nsy.club.controller;

import com.nsy.club.security.dto.ClubAuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    /**
     * 로그인 하지 않은 사용자도 접근
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll(){
        log.info("exAll............");
    }

    /**
     * 로그인한 사용자만 접근
     *
     * @AuthenticationPrincipal : 로그인한 사용자 정보(name + Account정보)를 어노테이션을 통해 간편하게 받을 수 있음
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO){
        log.info("exMember..........");
        log.info("--------------------------------");
        log.info(clubAuthMemberDTO);
    }

    /**
     * 관리자(admin)권한이 있는 사용자만 접근
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin............");
    }

    /**
     * 특정 사용자만 접근 가능
     * @param clubAuthMember
     * @return
     */
    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \"user95@nsy.org\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO  clubAuthMember){

        log.info("exMemberOnly...............");
        log.info(clubAuthMember);

        return "/sample/admin";
    }
}
