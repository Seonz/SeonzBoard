package sec02.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")
public class MemberController extends HttpServlet 
/* 
 * extends : 클래스 상속을 나타내는 키워드
 * HttpServlet : JavaServlet API에서 제공하는 추상 클래스로, 
 * HTTP 프로토콜을 기반으로 동작하는 웹 애플리케이션을 개발할 때 사용됨
 */
{
	private static final long serialVersionUID = 1L; //
	MemberDAO memberDAO;
   
	public void init() throws ServletException 
	/* init() : 서블릿 초기화 매개변수 읽기, 외부 리소스 로딩, 데이터 베이서 연결 설정
	기타 초기화 작업 */
	{
		memberDAO = new MemberDAO(); //MemberDAO 생성
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		doHandle(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		doHandle(request, response);
	}
	private void doHandle(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		/* 웹 애플리케이션에서 사용되는 HttpServletRequest 객체의 문자 인코딩을 설정하는 메소드
		 * 클라이언트가 전송한 요청 데이터의 문자 인코딩을 명시적으로 "utf-8로 설정한느 역할을 함
		 * 서버는 요청 데이터를 올바르게 해석하고 처리할 수 있게 됨
		 */
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo();
		System.out.println("action:" +action);
		if(action == null || action.equals("/listMembers.do"))
		{
			
		}
		List<MemberVO> membersList = memberDAO.listMembers(); 
		// 요청에 대해 회원 정보 조회
		request.setAttribute("membersList", membersList); 
		// 조회한 회원 정보를 request에 바인딩 함
		RequestDispatcher dispatch 
		= request.getRequestDispatcher("/test01/listMembers.jsp");
		dispatch.forward(request, response);
		// 컨트롤러에서 표시하고자 하는 JSP로 포워딩 함
		// 포워딩 : 클라이언트의 요청을 다른 리소스(페이지, 서블릿, JSP 등)로 전달하는 기능 
	}
}
