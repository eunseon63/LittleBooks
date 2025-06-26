package member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class MemberList extends AbstractController {

	private MemberDAO mdao;

	public MemberList() {
		mdao = new MemberDAO_imple();
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 사용자가 주소창에 직접 입력한 경우에는 접근되지 않게 메인 화면으로 이동
		String referer = request.getHeader("Referer");
		
		if(referer == null) {
			super.setRedirect(true);
			super.setViewPage(request.getContextPath() + "/index.go");
			return; // 종료
		}
		
		// 관리자로 로그인한 경우에만 회원 조회 가능
		HttpSession session = request.getSession();

		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		if (loginuser != null && "admin".equals(loginuser.getUserid())) {
			
			String sizePerPage = request.getParameter("sizePerPage");
			String currentShowPageNo = request.getParameter("currentShowPageNo");
			String searchType = request.getParameter("searchType");
			String searchWord = request.getParameter("searchWord");
			
			if (searchType == null
					|| (!"name".equals(searchType) && !"userid".equals(searchType) && !"email".equals(searchType))) {
				searchType = "";
			}

			if (searchWord == null || (searchWord != null && searchWord.trim().isEmpty())) {
				searchWord = "";
			}

			if (sizePerPage == null
					|| (!"10".equals(sizePerPage) && !"5".equals(sizePerPage) && !"3".equals(sizePerPage))) {
				sizePerPage = "10";
			}

			if (currentShowPageNo == null) {
				currentShowPageNo = "1";
			}
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("sizePerPage", sizePerPage);
			paraMap.put("currentShowPageNo", currentShowPageNo);
			paraMap.put("searchType", searchType);
			paraMap.put("searchWord", searchWord);
			
			int totalPage = mdao.getTotalPage(paraMap);
			
			// 사용자가 주소창에서 currentShowPageNo 값에 올바르지 않은 값을 입력한 경우
			try {

				if (Integer.parseInt(currentShowPageNo) > totalPage || Integer.parseInt(currentShowPageNo) <= 0) {
					currentShowPageNo = "1";
				}

			} catch (NumberFormatException e) {
				currentShowPageNo = "1";
			}
			
			List<MemberVO> memberList = mdao.select_Member_paging(paraMap);
			
			request.setAttribute("memberList", memberList);

			request.setAttribute("sizePerPage", sizePerPage);
			request.setAttribute("searchType", searchType);
			request.setAttribute("searchWord", searchWord);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/admin/memberList.jsp");
			
			String pageBar = "";

			int blockSize = 10;
			// blockSize는 블럭(토막)당 보여지는 페이지 번호의 개수

			int loop = 1;
			// loop 는 1 부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도이다.

			// ==== !!! 다음은 pageNo 구하는 공식이다. !!! ==== //
			int pageNo = ((Integer.parseInt(currentShowPageNo) - 1) / blockSize) * blockSize + 1;

			// *** [맨처음] [이전] 만들기 ***
			pageBar += "<li class='page-item'><a class='page-link' href='memberList.go?sizePerPage=" + sizePerPage
					+ "&currentShowPageNo=1" + "&searchType=" + searchType + "&searchWord=" + searchWord + "'>"
					+ "[맨처음]" + "</a></li>";

			if (pageNo != 1) {
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.go?sizePerPage=" + sizePerPage
						+ "&currentShowPageNo=" + (pageNo - 1) + "&searchType=" + searchType + "&searchWord="
						+ searchWord + "'>" + "[이전]" + "</a></li>";
			}

			while (!(loop > blockSize || pageNo > totalPage)) {

				if (pageNo == Integer.parseInt(currentShowPageNo)) {

					pageBar += "<li class='page-item active'><a class='page-link' href='memberList.go?sizePerPage="
						    + sizePerPage + "&currentShowPageNo=" + pageNo + "&searchType=" + searchType + "&searchWord="
						    + searchWord + "'>" + pageNo + "</a></li>";

				} else {

					pageBar += "<li class='page-item'><a class='page-link' href='memberList.go?sizePerPage="
							+ sizePerPage + "&currentShowPageNo=" + pageNo + "&searchType=" + searchType
							+ "&searchWord=" + searchWord + "'>" + pageNo + "</a></li>";

				}

				loop++; // 1 2 3 4 5 6 7 8 9 10

				pageNo++;

			}

			// *** [다음] [마지막] 만들기 ***
			if (pageNo <= totalPage) {
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.go?sizePerPage=" + sizePerPage
						+ "&currentShowPageNo=" + pageNo + "&searchType=" + searchType + "&searchWord=" + searchWord
						+ "'>" + "[다음]" + "</a></li>";
			}
			pageBar += "<li class='page-item'><a class='page-link' href='memberList.go?sizePerPage=" + sizePerPage
					+ "&currentShowPageNo=" + totalPage + "&searchType=" + searchType + "&searchWord=" + searchWord
					+ "'>" + "[마지막]" + "</a></li>";
			request.setAttribute("pageBar", pageBar);

			// === 페이지바 만들기 끝 ===

			/* >>> 뷰단(memberList.jsp)에서 "페이징 처리시 보여주는 순번 공식" 에서 사용하기 위해 
            	   검색이 있는 또는 검색이 없는 회원의 총개수 알아오기 시작 <<< */
			int totalMemberCount = mdao.getTotalMemberCount(paraMap);
			request.setAttribute("totalMemberCount", totalMemberCount);
			request.setAttribute("currentShowPageNo", currentShowPageNo);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/admin/memberList.jsp");
		}

		else {
			// 로그인을 안한 경우 또는 일반사용자로 로그인 한 경우
			String message = "관리자만 접근이 가능합니다.";
			String loc = "javascript:history.back()";

			request.setAttribute("message", message);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}

	}

}
