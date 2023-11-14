package hello.jdbc.connection;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionTest {

	@Test
	void driverManager() throws SQLException {
		Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		log.info("connection={}, class={}", con1, con1.getClass());
		log.info("connection={}, class={}", con2, con2.getClass());
	}

	@Test
	void dataSourceDriverManager() throws SQLException {
		// DriverManagerDataSource - 亲惑 货肺款 目池记 裙垫
		DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
		useDataSource(dataSource);
	}

	@Test
	void dataSourceConnectionPool() throws SQLException, InterruptedException {
		// 目池记 钱傅: HikariProxyConnection(Proxy) -> JdbcConnection(Target)
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		dataSource.setMaximumPoolSize(10);
		dataSource.setPoolName("MyPool");
		useDataSource(dataSource);
		Thread.sleep(1000); // 目池记 钱俊辑 目池记 积己 矫埃 措扁
	}

	private void useDataSource(DataSource dataSource) throws SQLException {
		Connection con1 = dataSource.getConnection();
		Connection con2 = dataSource.getConnection();
		log.info("connection={}, class={}", con1, con1.getClass());
		log.info("connection={}, class={}", con2, con2.getClass());
	}
}