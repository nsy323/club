package com.nsy.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * DTO역할을 수행하는 클래스인 동시에 스플이 시큐리티에서 인가/인증 작업에 사용 할 수 있음
 * (password는 부모 클래스를 사용하므로 별도의 멤버변수를 선언하지 않음)
 */

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User implements OAuth2User {

    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    private Map<String, Object> attr;

    //생성자
    public ClubAuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attr){

        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    public ClubAuthMemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities){

        super(username, password, authorities); //부모 생성자 상속

        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;
    }

    /**
     * 모든 인증결과 가져오기
     * @return
     */
    @Override
    public Map<String, Object> getAttributes(){
        return this.attr;
    }
}
