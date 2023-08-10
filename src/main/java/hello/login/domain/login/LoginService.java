package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     *
     * @return 이 null이면 로그인 실패
     */
    public Member login(String loginId, String password) {
        Optional<Member> findMemberOptional = memberRepository.findByLonginId(loginId);
        Member member = findMemberOptional.get();

        if (member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }

        //return memberRepository.findByLonginId(loginId).filter(m -> m.getPassword().equals(password)).orElse(null);
        //위의 if문을 합쳐놓은 것
    }
}
