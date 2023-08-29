package hello.login.web.filter;

import hello.login.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};
    //필터가 적용되지 않을 url을 배열로 만들어둠 css도 포함해줘야 화면이 깨지지 않음

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작={}", requestURI);

            if (isLoginCheckPath(requestURI)) { //요청 온 url이 whitelist에 있냐
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {

                    log.info("미인증 사용자 요청{}", requestURI);
                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    //단순 redirect를 사용해도 되지만 ("/login?redirectURL=" + requestURI) 이렇게 할 경우 로그인 후 원래 들어가려 했던 페이지로 바로 보내줌
                    return; //여기서 끝내겠다는 의미
                }
            }
        filterChain.doFilter(request, response);

        } catch (Exception e){
            throw e; //예외 로깅 가능하지만 톰캣까지 예외를 보내줘야 함
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }

    /**
     * 화이트 리스트의 경우 인증 체크x
     * 화이트 리스트에 있는지 확인 후 필터에 포함하는 url인지 검증하는 로직
     */

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
        //whitelist와 requestURI가 패턴에 매칭이 되는지 확인
    }

    //init, destory는 인터페이스 안에 들어가보면 default로 되어있음 default로 되어있는 건 구현할 필요가 없으면 굳이 구현하지 않아도 됨
}
