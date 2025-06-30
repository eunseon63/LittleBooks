package myshop.controller;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import common.controller.AbstractController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import member.domain.MemberVO;
import myshop.domain.BookVO;
import myshop.domain.CategoryVO;
import myshop.domain.SpecVO;
import myshop.model.BookDAO;
import myshop.model.BookDAO_imple;

public class BookRegister extends AbstractController {

    private BookDAO bdao = null;

    public BookRegister() {
        bdao = new BookDAO_imple();
    }

    private String extractFileName(String partHeader) {
        for (String cd : partHeader.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf("=") + 1).trim().replace("\"", "");
                int index = fileName.lastIndexOf(File.separator);
                return fileName.substring(index + 1);
            }
        }
        return null;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

        if (loginuser != null && "admin".equals(loginuser.getUserid())) {

            String method = request.getMethod();

            if (!"POST".equalsIgnoreCase(method)) {
                // GET : 등록 폼 보여주기
                List<CategoryVO> categoryList = bdao.getCategoryList();
                List<SpecVO> specList = bdao.getSpecList();

                request.setAttribute("categoryList", categoryList);
                request.setAttribute("specList", specList);

                super.setRedirect(false);
                super.setViewPage("/WEB-INF/myshop/admin/bookRegister.jsp");

            } else {
                // POST : 등록 처리

                ServletContext svlCtx = session.getServletContext();
                String uploadDir = svlCtx.getRealPath("/images");

                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                String bimage = null;

                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    if (part.getSubmittedFileName() != null && part.getSize() > 0) {
                        String originalFileName = part.getSubmittedFileName();
                        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

                        // 유니크 파일명 생성
                        String newFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."))
                                + "_" + String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS", Calendar.getInstance())
                                + System.nanoTime() + ext;

                        // 파일 저장
                        part.write(uploadDir + File.separator + newFileName);
                        part.delete();

                        if ("bimage".equals(part.getName())) {
                            bimage = newFileName;
                        }
                    }
                }

                String bname = request.getParameter("bname");
                String bcontent = request.getParameter("bcontent");
                int price = Integer.parseInt(request.getParameter("price"));
                int bqty = Integer.parseInt(request.getParameter("bqty"));
                String author = request.getParameter("author");

                int fk_publishseq = parseOrZero(request.getParameter("fk_publishseq"));
                int fk_categoryseq = parseOrZero(request.getParameter("fk_categoryseq"));
                int fk_snum = parseOrZero(request.getParameter("fk_snum"));

                int bookseq = bdao.getBookseq();

                BookVO bvo = new BookVO();
                bvo.setBookseq(bookseq);
                bvo.setBname(bname);
                bvo.setBcontent(bcontent);
                bvo.setPrice(price);
                bvo.setBqty(bqty);
                bvo.setAuthor(author);
                bvo.setBimage(bimage);
                bvo.setFk_publishseq(fk_publishseq);
                bvo.setFk_categoryseq(fk_categoryseq);
                bvo.setFk_snum(fk_snum);

                int n = bdao.bookInsert(bvo);

                if (n == 1) {
                    request.setAttribute("message", "책 등록 성공!");
                    request.setAttribute("loc", "/LittleBooks/");
                } else {
                    request.setAttribute("message", "책 등록 실패!");
                    request.setAttribute("loc", "javascript:history.back()");
                }

                super.setRedirect(false);
                super.setViewPage("/WEB-INF/msg.jsp");
            }

        } else {
            request.setAttribute("message", "관리자만 접근 가능합니다.");
            request.setAttribute("loc", "javascript:history.back()");
            super.setRedirect(false);
            super.setViewPage("/WEB-INF/msg.jsp");
        }

    }

    private int parseOrZero(String str) {
        try {
            return (str != null && !str.isEmpty()) ? Integer.parseInt(str) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
