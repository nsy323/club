package com.nsy.club.repository;

import com.nsy.club.entity.ClubMember;
import com.nsy.club.entity.ClubMemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 회원(ClubMember)테이블에 회원 100개 insert
     */
    @Test
    public void insertDummies(){

        //1 ~ 80 까지만 USER만 지정
        //81 ~ 90까지는 USER, MANAGER
        //91 ~ 100까지는 USER, MANAGER, ADMIN
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@nsy.org")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            //defaust role
            clubMember.addMemberRole(ClubMemberRole.USER);

            if(i > 80) clubMember.addMemberRole(ClubMemberRole.MANAGER);

            if(i > 90) clubMember.addMemberRole(ClubMemberRole.ADMIN);

            repository.save(clubMember);    //inesrt
        });
    }

    /**
     * 회원(ClubMember)테이블 한건 조회
     */
    @Test
    public void testRead(){
        Optional<ClubMember> result = repository.findByEmail("user23@nsy.org", false);

        ClubMember clubMember = result.get();

        System.out.println(clubMember);

    }
}
