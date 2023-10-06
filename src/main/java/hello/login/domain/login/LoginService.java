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
        Optional<Member> findMemberOptional = memberRepository.findByLonginId(loginId); //findById로 회원이 있는지 먼저 찾음
        Member member = findMemberOptional.get(); //optional에 담긴 findMember를 get으로 꺼냄

        if (member.getPassword().equals(password)) {
            return member; //member에서 가져온 password와 입력한 password가 같으면 member를 반환
        } else {
            return null; //같지 않으면 null을 반환 -> null은 로그인 실패
        }

        //return memberRepository.findByLonginId(loginId).filter(m -> m.getPassword().equals(password)).orElse(null);
        //위의 if문을 합쳐놓은 것
    }
}
