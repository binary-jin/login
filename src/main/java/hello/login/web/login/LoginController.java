package hello.login.web.login;

import hello.login.SessionConst;
import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.*;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginform";
    }

    //@PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) { //에러가 있는 경우
            return "login/loginForm";
        }
        //성공로직
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리

        //쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        //new Cookie(name, value)에서 value에는 string이 들어가야해서 String으로 타입을 변환시켜줌
        response.addCookie(idCookie); //응답에 쿠키를 넣어줌(HttpServletResponse에 쿠키를 추가해줌)

        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) { //에러가 있는 경우
            return "login/loginForm";
        }
        //성공로직
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리

        //세션 관리자를 통해 세션을 생성하고 회원 데이터를 보관
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) { //에러가 있는 경우
            return "login/loginForm";
        }
        //성공로직
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //getSession(true) ->  기본 값, 세션이 있으면 기존 세션을 반환, 세션이 없으면 새로운 세션 생성 반환
        //getSession(false) -> 세션 있으면 기존 세션 반환, 세션 없으면 새로운 세션 생성하지 않고 null 반환
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        //세션에 보관하고싶은 객체를 담아두면 됨

        //세션 관리자를 통해 세션을 생성하고 회원 데이터를 보관
        //sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL, //기본 값을 /로 설정해놔서 아무 것도 없을 땐 홈을 감
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) { //에러가 있는 경우
            return "login/loginForm";
        }
        //성공로직
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //getSession(true) ->  기본 값, 세션이 있으면 기존 세션을 반환, 세션이 없으면 새로운 세션 생성 반환
        //getSession(false) -> 세션 있으면 기존 세션 반환, 세션 없으면 새로운 세션 생성하지 않고 null 반환
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        //세션에 보관하고싶은 객체를 담아두면 됨

        //세션 관리자를 통해 세션을 생성하고 회원 데이터를 보관
        //sessionManager.createSession(loginMember, response);

        return "redirect:" + redirectURL;
        //위에 requestParam에 넘어오는 requestURL을 받을 수 있게 했으니 아무 것도 없으면 홈 화면, reqeustURL이 있으면 뒤의 URL로 감
    }

   //@PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate(); //안에 있는 걸 다 날려버림
        }

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
