package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
public class MemberDAO {
	private DataSource dataFactory;
	private Connection conn; 
	/*
	 * java에서 데이터베이스와의 연결을 나타내는 인터페이스
	 * 데이터베이스와의 연결을 설정하고 SQL 문을 실행하고 데이터베이스로부터 결과를 가져오는 등의 작업을 수행할 수 있음
	 */
	private PreparedStatement pstmt;
	
	public MemberDAO()
	{
		try
		{
			Context ctx = new InitialContext(); 
			/* 
			 * InitialContext : 애플리케이션 서버 환경에서 주로 사용되며,
			 * Java EE 애플리케이션의 JNDI 리소스에 액세스하는데 필요한 초기 컨텍스트를 설정하는데 사용됨
			 */
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			/*
			 * JNDI를 사용하여 "java:/comp/env" JNDI 이름으로 환경 컨텍스트(Context)를 검색하는 코드
			 * JNDI => Java 애플리케이션에서 네이밍 서비스 및 디렉토리 서비스를 검색하고 액세스하기 위한 API
			 * "java:/comp/env" => JNDI 네임 스페이스에서 표준적으로 사용되는 이름으로, Java EE 환경에서
			 * 애플리케이션의 리소스에 액세스하기 위해 사용됨
			 */
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
			/*
			 * JNDI를 사용하여 "jdbc/oracle" JNDI 이름으로 등록된 DataSource를 찾는 코드
			 * envContext는 JNDI "java:/comp/env" 네임 스페이스로부터 환경 컨텍스트를 검색한 객체 
			 */
		}catch (Exception e)
		{
			e.printStackTrace();
			/*
			 * java에서 예외의 스택 트레이스를 출력하는 메서드 예외가 발생했을 때 디버깅 및 오류 분석을 위해 사용됨
			 */
		}
	}
	public List<MemberVO> listMembers()
	{
		List<MemberVO> membersList = new ArrayList<MemberVO>();
		try {
			conn = dataFactory.getConnection();
			String query = "select * from t_member order by joinDate desc";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				MemberVO memberVO = new MemberVO(id, pwd, name, email, joinDate);
				membersList.add(memberVO);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return membersList;
	}
	public void addMember(MemberVO m)
	{
		try {
			conn = dataFactory.getConnection();
			String id = m.getId();
			String pwd = m.getPwd();
			String name = m.getName();
			String email = m.getEmail();
			String query = "INSERT INTO t_member(id, pwd, name, email)" + "VALUES(?,?,?,?)";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}