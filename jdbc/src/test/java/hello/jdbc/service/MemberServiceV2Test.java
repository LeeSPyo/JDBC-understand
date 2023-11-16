package hello.jdbc.service;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.sevice.MemberServiceV2;

/**
 * Ʈ����� - Ŀ�ؼ� �Ķ���� ���� ��� ����ȭ
 */
class MemberServiceV2Test {
	private MemberRepositoryV2 memberRepository;
	private MemberServiceV2 memberService;

	@BeforeEach
	void before() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
		memberRepository = new MemberRepositoryV2(dataSource);
		memberService = new MemberServiceV2(dataSource, memberRepository);
	}

	@AfterEach
	void after() throws SQLException {
		memberRepository.delete("memberA");
		memberRepository.delete("memberB");
		memberRepository.delete("ex");
	}

	@Test
	@DisplayName("���� ��ü")
	void accountTransfer() throws SQLException {
		
		//given
		Member memberA = new Member("memberA", 10000);
		Member memberB = new Member("memberB", 10000);
		memberRepository.save(memberA);
		memberRepository.save(memberB);
		
		//when
		memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
		
		//then
		Member findMemberA = memberRepository.findById(memberA.getMemberId());
		Member findMemberB = memberRepository.findById(memberB.getMemberId());
		assertThat(findMemberA.getMoney()).isEqualTo(8000);
		assertThat(findMemberB.getMoney()).isEqualTo(12000);
	}

	@Test
	@DisplayName("��ü�� ���� �߻�")
	void accountTransferEx() throws SQLException {
		
		//given
		Member memberA = new Member("memberA", 10000);
		Member memberEx = new Member("ex", 10000);
		memberRepository.save(memberA);
		memberRepository.save(memberEx);
		
		//when
		assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
				.isInstanceOf(IllegalStateException.class);
		
		//then
		Member findMemberA = memberRepository.findById(memberA.getMemberId());
		Member findMemberEx = memberRepository.findById(memberEx.getMemberId());
		
		//memberA�� ���� �ѹ� �Ǿ����
		assertThat(findMemberA.getMoney()).isEqualTo(10000);
		assertThat(findMemberEx.getMoney()).isEqualTo(10000);
	}
}