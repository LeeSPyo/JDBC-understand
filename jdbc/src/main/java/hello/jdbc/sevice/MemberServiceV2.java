package hello.jdbc.sevice;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
	private final DataSource dataSource;
	private final MemberRepositoryV2 memberRepository;

	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		Connection con = dataSource.getConnection();
		try {
			con.setAutoCommit(false); // Ʈ����� ����
			//����Ͻ� ����
			bizLogic(con, fromId, toId, money);
			con.commit(); // ������ Ŀ��
		} catch (Exception e) {
			con.rollback(); // ���н� �ѹ�
			throw new IllegalStateException(e);
		} finally {
			release(con);
		}
	}

	private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
		Member fromMember = memberRepository.findById(con, fromId);
		Member toMember = memberRepository.findById(con, toId);
		memberRepository.update(con, fromId, fromMember.getMoney() - money);
		memberRepository.update(con, toId, toMember.getMoney() + money);
	}

	private void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")) {
			throw new IllegalStateException("��ü�� ���� �߻�");
		}
	}

	private void release(Connection con) {
		if (con != null) {
			try {
				con.setAutoCommit(true); // Ŀ�ؼ� Ǯ ����
				con.close();
			} catch (Exception e) {
				log.info("error", e);
			}
		}
	}
}