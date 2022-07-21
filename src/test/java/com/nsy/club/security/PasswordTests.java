package com.nsy.club.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * BcryptPasswordEncode 테스트
     */
    @Test
    @DisplayName("패스워드 암호화 테스트")
    public void testEncode(){

        String password = "1111";

        String enPw = passwordEncoder.encode(password);
        System.out.println("enPw : " + enPw);

        boolean matchResult = passwordEncoder.matches(password, enPw);

        System.out.println("matchResult : " + matchResult);
    }


}
