package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        //위에 파라미터로 들어가있는 ServletRequest는 기능이 별로 없어서 HttpServletRequest로 다운캐스팅 해줌(HttpServletRequest의 부모가 servletRequest임)
        String requestURI = httpRequest.getRequestURI(); //모든 사용자의 요청 URI를 남김

        String uuid = UUID.randomUUID().toString(); //사용자들을 구분하기 위해 uuid를 남김

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI); //모든 요청에 대해 로그 남김
            filterChain.doFilter(request, response); //이게 빠지면 다음 필터 호출이 안 되고 여기서 끝나버림
        }catch (Exception e) {
            throw e;
        }finally{
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
