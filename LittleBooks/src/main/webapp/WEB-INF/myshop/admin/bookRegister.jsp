<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../../header1.jsp" />

<!-- Bootstrap 4 CSS + JS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<style>
  body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: #333;
    padding-top: 30px;
    padding-bottom: 50px;
  }

  .container-custom {
    background: #fffbea;
    padding: 30px 40px;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(255, 204, 0, 0.15);
    max-width: 1000px;
    margin: auto;
  }

  h2.page-title {
    font-weight: 700;
    color: #b37b00;
    margin-bottom: 40px;
    border-bottom: 3px solid #f0c419;
    padding-bottom: 10px;
    text-align: center;
    letter-spacing: 1.5px;
  }

  span.error {
    color: #d42e2e;
    font-weight: 700;
    font-size: 0.9rem;
    margin-left: 8px;
    display: none;
  }

  .bookInputName {
    background: #fff5b1 !important;
    font-weight: 600;
    color: #7a5900 !important;
    width: 25%;
  }

  .form-wrapper {
    display: flex;
    flex-wrap: nowrap;
    gap: 30px;
    align-items: flex-start;
  }

  .form-left {
    flex: 0 0 40%;
    min-width: 300px;
  }

  .form-right {
    flex: 1 1 60%;
    min-width: 400px;
  }

  .image-preview {
	  text-align: center;
	  padding: 20px;
	  border: 2px dashed #f0c419;
	  border-radius: 10px;
	  background-color: #fff5cc;
	  cursor: pointer;
	  height: 500px; /* ← 여기 숫자만 바꾸면 됩니다 */
	  display: flex;
	  flex-direction: column;
	  justify-content: center;
	  align-items: center;
	  transition: background-color 0.3s, border-color 0.3s;
	}

  .image-preview.dragover {
    background-color: #ffeaa7;
    border-color: #d4af37;
  }

  #previewImg {
    border: 2px solid #f0c419;
    border-radius: 10px;
    margin-top: 10px;
    max-width: 100%;
    max-height: 400px;
    height: auto;
    box-shadow: 0 2px 10px rgba(255, 215, 0, 0.3);
    display: block;
  }
</style>

<script>
  let total_fileSize = 0;
  let file_arr_copy = [];

  function updatePreviewAndFileName(file) {
    if (!(file.type === "image/jpeg" || file.type === "image/png")) {
      alert("jpg 또는 png 파일만 가능합니다.");
      return;
    }

    if (file.size >= 10 * 1024 * 1024) {
      alert("10MB 이상인 이미지는 업로드 불가합니다.");
      return;
    }

    file_arr_copy = [file];
    total_fileSize = file.size;

    const fileReader = new FileReader();
    fileReader.readAsDataURL(file);
    fileReader.onload = function () {
      $("#previewImg").attr("src", fileReader.result);
    };

    $("#fileNameDisplay").val(file.name);

    // 폼 제출용 input 갱신
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(file);
    $("input[name='bimage']")[0].files = dataTransfer.files;
  }

  $(function () {
    $("span.error").hide();

    $("input[name='bimage']").on("change", function (e) {
      const file = e.target.files[0];
      if (file) updatePreviewAndFileName(file);
    });

    $("#fileDrop")
      .on("dragenter dragover", function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).addClass("dragover");
      })
      .on("dragleave drop", function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).removeClass("dragover");
      })
      .on("drop", function (e) {
        const files = e.originalEvent.dataTransfer.files;
        if (files && files.length > 0) updatePreviewAndFileName(files[0]);
      })
      .on("click", function (e) {
        if (!$(e.target).is("input")) {
          $("input[name='bimage']").trigger("click");
        }
      });

    $("form[name='bookInputFrm']").on("submit", function (e) {
      $("span.error").hide();
      let isValid = true;

      $(".infoData").each(function () {
        if ($(this).val().trim() === "") {
          $(this).next(".error").show();
          isValid = false;
        }
      });

      if (!isValid || total_fileSize >= 20 * 1024 * 1024) {
        if (total_fileSize >= 20 * 1024 * 1024) {
          alert("첨부한 파일의 총합이 20MB를 초과하였습니다.");
        }
        e.preventDefault();
      }
    });

    $("input[type='reset']").on("click", function () {
      $("span.error").hide();
      file_arr_copy = [];
      total_fileSize = 0;
      $("#previewImg").attr("src", "");
      $("#fileNameDisplay").val("");
    });
  });
</script>

<div class="container-custom" style="margin-top: 100px;">
  <h2 class="page-title">도서등록 [관리자전용]</h2>
  <form name="bookInputFrm" enctype="multipart/form-data" method="post" action="bookRegister.go">
    <div class="form-wrapper">
      <!-- 왼쪽: 이미지 미리보기 -->
      <div class="form-left">
        <div id="fileDrop" class="image-preview" title="사진을 클릭하거나 드래그하세요">
          <strong>책 이미지를 드래그하거나 클릭하여 선택하세요</strong>
          <img id="previewImg" alt="미리보기 이미지" />
          <input type="file" name="bimage" class="infoData" accept="image/jpeg,image/png" style="display: none" />
        </div>
      </div>

      <!-- 오른쪽: 도서 정보 입력 -->
      <div class="form-right">
        <table class="table table-borderless">
          <tbody>
            <tr>
              <th class="bookInputName">카테고리<span class="text-danger">*</span></th>
              <td>
                <select name="fk_categoryseq" class="form-control infoData">
				  <option value="">:::선택하세요:::</option>
				  <c:forEach var="cvo" items="${requestScope.categoryList}">
				    <option value="${cvo.categoryseq}">${cvo.categoryname}</option>
				  </c:forEach>
				</select>
                <span class="error">필수입력</span>
              </td>
            </tr>
            <tr>
              <th class="bookInputName">도서명<span class="text-danger">*</span></th>
              <td>
                <input type="text" name="bname" class="form-control infoData" />
                <span class="error">필수입력</span>
              </td>
            </tr>
            <tr>
			  <th class="bookInputName">출판사<span class="text-danger">*</span></th>
			  <td>
			    <select name="fk_publishseq" class="form-control infoData" style="max-width: 300px;">
			      <option value="">:::선택하세요:::</option>
			      <option value="1">푸른책방</option>
			      <option value="2">행복출판사</option>
			      <option value="3">꿈나무출판</option>
			    </select>
			    <span class="error">필수입력</span>
			  </td>
			</tr>
			<tr>
			  <th class="bookInputName">저자<span class="text-danger">*</span></th>
			  <td>
			    <input type="text" name="author" class="form-control infoData" />
			    <span class="error">필수입력</span>
			  </td>
			</tr>
            <tr>
              <th class="bookInputName">재고수량<span class="text-danger">*</span></th>
              <td>
                <input type="number" name="bqty" value="1" class="form-control infoData" style="max-width: 100px;" />
                <span class="error">필수입력</span>
              </td>
            </tr>
            <tr>
              <th class="bookInputName">가격<span class="text-danger">*</span></th>
              <td>
                <input type="number" name="price" class="form-control infoData" style="max-width: 200px;" />
                <span class="error">필수입력</span>
              </td>
            </tr>
            <tr>
              <th class="bookInputName">도서스펙<span class="text-danger">*</span></th>
              <td>
                <select name="fk_snum" class="form-control infoData" style="max-width: 300px;">
                  <option value="">:::선택하세요:::</option>
                  <c:forEach var="svo" items="${requestScope.specList}">
                    <option value="${svo.snum}">${svo.sname}</option>
                  </c:forEach>
                </select>
                <span class="error">필수입력</span>
              </td>
            </tr>
            <tr>
              <th class="bookInputName">도서이미지<span class="text-danger">*</span></th>
              <td>
                <input type="text" id="fileNameDisplay" class="form-control" readonly placeholder="선택된 이미지 파일 이름" />
                <span class="error">필수입력</span>
              </td>
            </tr>
            <tr>
              <th class="bookInputName">도서설명</th>
              <td>
                <textarea name="bcontent" rows="5" class="form-control"></textarea>
              </td>
            </tr>
            <tr>
              <td colspan="2" class="text-center pt-4">
                <input type="submit" value="도서등록" class="btn btn-warning btn-lg mr-3" />
                <input type="reset" value="취소" class="btn btn-danger btn-lg" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </form>
</div>

<jsp:include page="../../footer.jsp" />
