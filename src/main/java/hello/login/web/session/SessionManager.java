package hello.login.web.session;


import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    //hashmap을 써도 되는데 동시성 문제가 있을 땐 ConcurrentHashMap 써야함

    /**
     * 세션 생성
     * sessionId 생성 (임의의 추정 불가능한 랜덤 값)
     * 세션 저장소에 sessionId와 보관할 값 저장
     * sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {

        //세션ID를 생성하고 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString(); //이걸 사용하면 랜덤한 값을 얻을 수 있음
        sessionStore.put(sessionId, value); //세션 저장소에 넘어온 세션 아이디와 value=member객체 자체를 넣어줌

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId); //mySessionId 편하게 사용하기 위해 상수로 만듬
        //쿠키 이름은 mySessionId, 값은 랜덤 토큰(sessionId)을 넣어줌
        response.addCookie(mySessionCookie);

    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) { //세션 쿠키가 null이면 null 반환
            return null;
        }
        return sessionStore.get(sessionCookie.getValue()); //세션 저장소에서 세션 쿠키에 해당되는 값(멤버 객체)를 반환함
    }

    /**
     *
     *  세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME); //세션 쿠키를 가져옴
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
            //세션 쿠키가 null이 아니면 세션 저장소에서 값을 다 지워버림
        }
    }

    //쿠키를 찾는 로직을 별도로 관리
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        //쿠키를 찾아보고 없으면 Null을 반환

        return Arrays.stream(request.getCookies()) //배열을 stream으로 바꿔줌
                .filter(cookie -> cookie.getName().equals(cookieName)) //쿠키의 값이 넘어온 cookiename과 같은지 확인
                .findAny().orElse(null); //있으면 쿠키를 반환, 없으면 null을 반환

    }

}
