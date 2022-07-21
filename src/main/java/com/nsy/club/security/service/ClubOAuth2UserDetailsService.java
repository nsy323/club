package com.nsy.club.security.service;

import com.nsy.club.entity.ClubMember;
import com.nsy.club.entity.ClubMemberRole;
import com.nsy.club.repository.ClubMemberRepository;
import com.nsy.club.security.dto.ClubAuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OAuth2UesrService 인터페이스 : 실제 소셜 로그인 과정에서 동작하는 존재, UserDetailsService의 OAuth(Open Authorization) 버전
 *
 *
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository repository;

    //암호화
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        log.info("----------------------------------");
        log.info("userReuest : " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName : " + clientName);     //Google출력
        log.info(userRequest.getAdditionalParameters()); //id_token 출력

        OAuth2User oAuth2User = super.loadUser(userRequest);


        /**
         * sub : 107158464740100874036
         * picture : https://lh3.googleusercontent.com/a/default-user=s96-c
         * email : nsy860504@gmail.com
         * email_verified : true
         */

        log.info("========================");

        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + " : " + v);
        });

        String email = null;

        if(clientName.equals("Google")){    //Google이용시
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL : " + email);

        ClubMember member = saveSocialMember(email); //회원가입된 사용자가 아닐 경우 회원가입 처리

        ClubAuthMemberDTO clubAuthMember = new ClubAuthMemberDTO(
                    member.getEmail(),
                    member.getPassword(),
                    true,
                    member.getRoleSet().stream().map(
                                role -> new SimpleGrantedAuthority("ROLE_" + role.name())
                            ).collect(Collectors.toList()),
                    oAuth2User.getAttributes()
        );

        clubAuthMember.setName(member.getName());

        return clubAuthMember;
    }

    /**
     * 소셜로그인한 이메일 처리
     */
    private ClubMember saveSocialMember(String email){

        Optional<ClubMember> result = repository.findByEmail(email, true);

        //가입된 email이 있을 경우
        if(result.isPresent()){
            return result.get();
        }

        //가입된 email이 없는 경우, 패스워드 : 1111, 이메일 : 소셜 email로  build
        ClubMember clubMember = ClubMember.builder()
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);

        repository.save(clubMember);

       return clubMember;
    }




}
