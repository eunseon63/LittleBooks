package common.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import book.domain.BookVO;

public class IndexController extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 1. 청소년 권장 도서 리스트 생성
        List<BookVO> youthBooks = new ArrayList<>();
        
        youthBooks.add(new BookVO("아낌없이 주는 나무", "셸 실버스타인 ", "아울북", "tree.jpeg", "우리나라에 단 하나뿐인 정식 한국어판 『아낌없이 주는 나무』, 새롭게 태어나다!"));
        youthBooks.add(new BookVO("어린왕자", "앙투안 마리 로제 드 생텍쥐페리", "문학동네", "prince.jpeg", "가장 중요한 건 눈에 보이지 않아! 이 세상에 남아 있는 마지막 순수와 아름다움을 간직한 어린왕자의 이야기. "));
        youthBooks.add(new BookVO("구름빵", "백희나", "한솔수북", "cloud.jpeg", "구름을 반죽해서 빵으로 만든다구? 기발한 상상력에 따뜻한 가족의 사랑을 버무린 구름빵 이야기!"));
        youthBooks.add(new BookVO("마법천자문", "알에스미디어", "아울북", "magic.jpeg", "대한민국 어린이들의 필독서이자 스토리로 즐기는 국내 유일의 한자 학습만화 『마법천자문』!"));
        youthBooks.add(new BookVO("무지개 물고기", "마르쿠스 피스터", "시공주니어", "rainbow.jpeg", "무지개 물고기와 친구들이 펼치는 바다 속 세상의 크고 작은 이야기! '네버랜드 세계의 걸작 그림책' 제33권 『무지개 물고기』. 스위스 태생의 베스트셀러 그림책 작가 마르쿠스 피스터의 그림책입니다."));
        youthBooks.add(new BookVO("100층짜리 집", "이와이 도시오", "북뱅크", "house.jpeg", "도치는 조심스럽게 문을 열고 100층짜리 집으로 들어갔어요. 그런데 1층부터 10층까지는 생쥐들이 살고 있었어요. 도치는 11층부터 21층까지는 누가 살고 있을지 궁금했는데……."));
       
        // 2. request에 담기 (JSTL에서 ${youthBooks}로 접근 가능)
        request.setAttribute("youthBooks", youthBooks);

        // 3. 뷰 페이지 설정 (포워딩)
        super.setRedirect(false);
        super.setViewPage("/WEB-INF/index.jsp");
	}

}
