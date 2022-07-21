package com.nsy.club.entity;

import lombok.*;
import org.hibernate.loader.entity.plan.AbstractLoadPlanBasedEntityLoader;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

/**
 * 회원
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity {

    @Id
    private String email;       //이메일

    private String password;    //비밀번호

    private String name;        //이름(닉네임)

    private boolean fromSocial; //소셜가입여부(소셜 로그인으로 회원가입된 경우)

    @ElementCollection  //JPA에게 컬렉션 객체임을 알려줌
    @Builder.Default    //특정 필드를 특정 값으로 초기화
    private Set<ClubMemberRole> roleSet = new HashSet<>();

    //권한 추가
    public void addMemberRole(ClubMemberRole clubMemberRole){
        roleSet.add(clubMemberRole);
    }

}
