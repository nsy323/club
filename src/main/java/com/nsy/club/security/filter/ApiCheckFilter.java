package com.nsy.club.security.filter;

import com.nsy.club.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.parameters.P;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/***
 *
 *  - Filter : 요청(request)과 응답(Response)에 대한 정보들을 변경할 수 있게 개발자들에게 제공하는 서블릿 컨테이너
 *  - FilterChain : Filter가 여러개 모여서 하나의 체인을 형성하는 것
 *  - doFilter(ServletRequest request, ServletResponse response, FilterChain chain) :
 *      이 메서드를 통해서 요청(Request)과 응답(Response) 쌍이 체인을 통과할 때마다 컨테이너에서 호출됨, 체인을 따라서 계속 다음에 존재하는 필터로 이동
 *
 */

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    /**
     * AntPathMatcher : 엔트패턴(Ant Pattern 중간에 ?,*, *.* 와 같은 기호를 이용해서 패턴을 표시)에 맞는지 검사하는 유틸리티
     */
    private AntPathMatcher antPathMatcher;

    private String pattern;

    /**
     * jjwt 암호화 및 검증
     */
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("REQUESTURI : " + request.getRequestURI());

        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        //uri 패턴이 일치하면
        if(antPathMatcher.match(pattern, request.getRequestURI())){
            log.info("ApiCheckFilter..........................");
            log.info("ApiCheckFilter..........................");
            log.info("ApiCheckFilter..........................");

            boolean checkHeader = checkAuthHeader(request); //Authorization 헤더값 체크

            //Authorization 헤더값에 특정값("12345678")이 포함되어 있으면
            if(checkHeader){
                filterChain.doFilter(request, response);
                return;
            }else{
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);       //금지

                //json리턴 및 한글깨짐 수정
                response.setContentType("application/json;charset=utf-8");

                JSONObject json = new JSONObject(); //json객체 생성

                String message = "FAIL CHECK API TOKEN";

                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더 처리
     *
     * Http헤더 메세지에 틀별한 값 지정하여 전송 -> Request에 포함된 Authorization 헤더값 파악해서 사용자 정상적인 요청인지 알아내는 것
     * @param request
     * @return
     */
    private boolean checkAuthHeader(HttpServletRequest request){
        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist : " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));

                log.info("validate result : " + email);

                checkResult = email.length()> 0;

            }catch(Exception e){
                e.printStackTrace();
            }

            //헤더 값에 12345678 이 포함되어 있으면
            //if(authHeader.equals("12345678")){
            //    checkResult = true;
            //}
        }
        return checkResult;
    }
}
