package hello.jdbc.repository;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariDataSource;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV1Test {

	MemberRepositoryV1 repository;

	@BeforeEach
	void beforeEach() throws Exception {
		
		// ±âº» DriverManager - Ç×»ó »õ·Î¿î Ä¿³Ø¼Ç È¹µæ
		// DriverManagerDataSource dataSource =
		// new DriverManagerDataSource(URL, USERNAME, PASSWORD);
		// Ä¿³Ø¼Ç Ç®¸µ: HikariProxyConnection -> JdbcConnection
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		repository = new MemberRepositoryV1(dataSource);
	}

	@Test
	void crud() throws SQLException, InterruptedException {
		
		log.info("start");
		
		// save
		Member member = new Member("memberV0", 10000);
		repository.save(member);
		
		// findById
		Member memberById = repository.findById(member.getMemberId());
		assertThat(memberById).isNotNull();
		
		// update: money: 10000 -> 20000
		repository.update(member.getMemberId(), 20000);
		Member updatedMember = repository.findById(member.getMemberId());
		assertThat(updatedMember.getMoney()).isEqualTo(20000);
		
		// delete
		repository.delete(member.getMemberId());
		assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
	}

}
