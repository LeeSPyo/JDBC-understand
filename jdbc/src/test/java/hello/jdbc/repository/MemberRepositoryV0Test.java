package hello.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV0Test {
	
	MemberRepositoryV0 repository = new MemberRepositoryV0();
	
	@Test 
	void crud() throws SQLException {
		//save
		Member member = new Member("memberV2", 10000);
		repository.save(member);
		
		//findById
		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember={}", findMember);
		log.info("member == findMember {}", member == findMember);
		assertThat(findMember).isEqualTo(member);
		
	}

}
