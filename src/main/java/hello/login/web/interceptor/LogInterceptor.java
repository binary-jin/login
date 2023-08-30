package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    //로그를 찍기 위한 인터셉터

    public static final String LOG_ID = "logId"; //logId를 상수로 뺌

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String requestURI = request.getRequestURI(); //모든 요청의 URI를 저장
        String uuid = UUID.randomUUID().toString(); //사용자를 구분하기 위해 uuid를 넣음

        request.setAttribute(LOG_ID, uuid); //afterCompletion으로 넘기기 위해 setAttribute로 담아서 넘김

        //@RequestMapping의 경우엔 HandlerMetohod 핸들러(컨트롤러)가 사용
        //정적 리소스의 경우엔 ResourceHttpRequestHandler 핸들러(컨트롤러)가 사용
        if (handler instanceof HandlerMethod) { //핸들러(컨트롤러)의 정보를 보려면 보통 이렇게
            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨ㅌ롤러 메서드의 모든 정보가 포함되어있음
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        //어떤 컨트롤러가 호출되는지 Handler도 볼 수 있음
        return true; //return false로 하게되면 여기서 끝, true로 하게되면 다음 컨트롤러 호출
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        log.info("postHandle [{}]", modelAndView);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //로그 response 할 때 uuid를 받아야하니 여기서 받아야함
        //예외가 발생하면 postHandle을 건너뛰고 afterCompletion으로 가기 때문에 uuid를 여기서 받아야함
        String requestURI = request.getRequestURI();
        Object logId = (String) request.getAttribute(LOG_ID); //preHandle에서 setAttribute로 담아둔 걸 getAttribute로 받음

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

        if (ex != null) { //예외가 터졌으면 예외를 로그로 출력
            log.error("afterCompletion error", ex);
        }
    }
}
