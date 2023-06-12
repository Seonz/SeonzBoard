package sec02.ex01;
/*
 * 컨트롤러 역할을 하는 MemberController클래스
 * getPathInfo() 메서드를 이용해 두 단계로 이루어진 요청을 가져옴
 * action 값에 따라 if문을 분기해서 요청한 작업을 수행하는데 action 값이 null이거나
 * ./listMembers.do 인 경우 회원 조회 기능을 수행함
 * 만약 action 값이 /memberForm.do면 회원가입창을 나타내고,
 * action 값이 /addMeber.do면 전송된 회원 정보들을 테이블에 추가함
 */
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
		String action = request.getPathInfo(); // URL에서 요청명을 가져옴
		System.out.println("action:" +action);
		if(action == null || action.equals("/listMembers.do"))
		//최초 요청이거나 action 값이 /memberList.do면 회원 목록을 출력
		{
			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test02/listMembers.jsp"; //test02 폭더의 listMember.jsp로 포워딩
		}else if(action.equals("/addMember.do")) // action 값이 /addMember.do면 전송된 회원 정보를 가져와서 테이블에 추가
		{
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO (id, pwd, name, email);
			memberDAO.addMember(memberVO);
			nextPage= "/member/listMembers.do"; //회원 등록 후 다시 회원 목록을 출력
		}else if (action.equals("/memberForm.do")) //action 값이 /memberForm.do면 회원 가입 창을 화면에 출력
		{
			nextPage = "/test02/memberForm.jsp"; //test02 폴더의 memberForm.jsp로 포워딩
		}else // 그 외 다른 action 값은 회원 목록을 출력
		{
			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test02/listMember.jsp";
		}
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage); //nextPage에 지정한 요청명으로 다시 서블릿에 요청함
		dispatch.forward(request, response);
	}
}
