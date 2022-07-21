package com.nsy.club.security;

import com.nsy.club.security.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class JWTTests {

    private JWTUtil jwtUtil;

    /**
     * @BeforeEach : 테스트 메서드 시행이전에 수행
     */
    @BeforeEach
    public void testBefore(){

        System.out.println("testBefore.....................");
        jwtUtil = new JWTUtil();
    }

    /**
     * jjwt를 이용하여 암호화된 문자열 추출
     * @throws Exception
     */
    @Test
    public void testEncode() throws Exception {
        String email = "user83@nsy.org";

        String str = jwtUtil.generateToken(email);

        System.out.println(str);
    }

    @Test
    public void testValidate() throws Exception {

        String email = "user83@nsy.org";

        String str = jwtUtil.generateToken(email);

        Thread.sleep(5000);

        String resultEmail = jwtUtil.validateAndExtract(str);

        System.out.println(resultEmail);
    }
}
