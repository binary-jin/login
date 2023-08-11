package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save:member={}", member);
        store.put(member.getId(), member); //member.getId로 찾고 store에 저장

        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLonginId(String loginId) {
//        List<Member> all = findAll();
//        for (Member m : all) {
//            if (m.getLoginId().equals(loginId)) {
//                return Optional.of(m);
//            }
//        }

//        return Optional.empty();
        //Optional은 Optional이라는 통이 있고 그 안에 member 객체를 넣어랴둘 수도 있고 안 넣을 수도 있음
        // -> null로 반환 될 수 있는 경우에 null을 반환하기보다 Optional을 사용함

        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
        //list를 stream으로 바꿈 루프를 돈다고 생각하면 됨 여기서 filter() 괄호 안에 만족하는 애만 다음 단계로 넘어감
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
