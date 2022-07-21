package com.nsy.club.security.util;

import ch.qos.logback.classic.Logger;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * - JWT(JSON Web Token)
 *
 * 1) Header - 토큰타입과 알고리즘을 의미, HS256 혹은 RSA
 * 2) Payload - 이름(name)과 값(value)의 쌍을 Claim이라고 하고, claims를 모아둔 객체를 의미
 * 3) Signature - 헤더의 인코딩 값과 정보의 인코딩 값을 합쳐 비밀키로 해시함수 처리된 결과
 *
 *
 */

@Log4j2
public class JWTUtil {

    private String secretKey = "nsy12345678";   //비밀키

    private long expire = 60 * 24 * 30;         //만료일(1month)

    /**
     * JWT토큰을 생성하는 역할
     * @param content
     * @return
     * @throws Exception
     */
    public String generateToken(String content) throws Exception{

        return Jwts.builder()       //Jwt Builder객체 생성
                .setIssuedAt(new Date())    //생성일 설정
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))  //만료일 설정(1달 후 만료)
//                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()))  //만료일 설정(1초 후 만료)
                .claim("sub", content)  //'sub'라는 이름을 가지는 Claim에 사용자 이메일 주소를 입력해 주어서 나중에 사용할 수 있도록 구성
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))    //JWT 서명을 위해 secretkey 지정
                .compact();         //JWS 생성
    }

    /**
     * 인코딩된 문자열에서 원하는 값을 추출하는 용도
     * JWT문자열을 검증하는 역할
     *
     * @param tokenStr
     * @return
     * @throws Exception
     */
    public String validateAndExtract(String tokenStr) throws Exception {

        String contentValue = null;

        try{

            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                    .setSigningKey(secretKey.getBytes("UTF-8"))
                    .parseClaimsJws(tokenStr);

            log.info(defaultJws);

            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("----------------------");

            contentValue = claims.getSubject();

        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }

        return contentValue;
    }

}
