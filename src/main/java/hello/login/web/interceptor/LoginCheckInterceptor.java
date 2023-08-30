package hello.login.web.interceptor;

import hello.login.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행{}", requestURI);

        HttpSession session = request.getSession(); //로그인 상태를 확인하기 위해 세션을 가져옴

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);
            //세션이 없거나 세션에 담긴 로그인 멤버가 null이면 미인증 사용자 요청으로 로그인 화면으로 다시 돌려보냄

            return false; //미인증 사용자 요청일 경우 여기서 끝내기 위해 return을  false로 지정
        }

        return true;
    }
}
