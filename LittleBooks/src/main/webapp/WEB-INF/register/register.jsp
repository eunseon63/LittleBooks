<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // context path 가져오기 (리소스 경로에 사용됨)
    String ctxPath = request.getContextPath();
%>

<!-- Google Web Font -->
<link href="https://fonts.googleapis.com/css2?family=Cafe24+Surround&display=swap" rel="stylesheet">

<!-- CSS 파일 링크 -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/css/member/memberRegister.css" />
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/bootstrap-4.6.2-dist/css/bootstrap.min.css" />

<!-- jQuery 및 UI 플러그인 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>

<!-- 우편번호 API -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<!-- 사용자 정의 스크립트 -->
<script type="text/javascript" src="<%= ctxPath %>/js/register/Register.js"></script>
<script type="text/javascript" src="<%= ctxPath %>/js/member/deleteMember.js"></script>

<!-- 글꼴 적용 -->
<style>
    body {
        font-family: 'Cafe24 Surround', '맑은 고딕', sans-serif;
    }
</style>

<!-- 공통 헤더 -->
<jsp:include page="../header1.jsp" />

<!-- 회원가입 폼 -->
<div class="container">
    <div class="row justify-content-center" id="divRegisterFrm">
        <div class="col-md-8">
            <form name="registerFrm">
                <table id="tblMemberRegister">
                    <thead>
                        <tr class="mb-3">
                            <th colspan="2">
                                ::: 회원가입 
                                <span style="font-size: 10pt; font-style: italic;">
                                    (<span class="star">*</span>표시는 필수입력!!)
                                </span> :::
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    	<br>
                    	<br>
                    	<br>
                    	<br>
                        <!-- 이름 입력 -->
                        <tr class="mb-3">
                            <td class="align-middle">이름&nbsp;<span class="star">*</span></td>
                            <td>
                                <input type="text" name="name" id="name" maxlength="30" autocomplete="off" class="requiredInfo" />
                                <span class="error">성명은 필수입력 사항입니다.</span>
                            </td>
                        </tr>

                        <!-- 아이디 입력 및 중복 확인 -->
                        <tr class="mb-3">
                            <td class="align-middle">아이디&nbsp;<span class="star">*</span></td>
                            <td>
                                <input type="text" name="userid" id="userid" maxlength="40"autocomplete="off" class="requiredInfo" />
                                <img src="<%= ctxPath %>/images/b_id_check.gif" id="idcheck" />
                                <span id="idcheckResult"></span>
                                <span class="error">아이디는 필수입력 사항입니다.</span>
                            </td>
                        </tr>

                        <!-- 비밀번호 입력 -->
                        <tr class="mb-3">
                            <td class="align-middle">비밀번호&nbsp;<span class="star">*</span></td>
                            <td>
                                <input type="password" name="pwd" id="pwd" maxlength="15" autocomplete="off" class="requiredInfo" />
                                <span class="error">암호는 영문자, 숫자, 특수기호가 혼합된 8~15 글자로 입력하세요.</span>
                            </td>
                        </tr>

                        <!-- 비밀번호 확인 -->
                        <tr class="mb-3">
                            <td class="align-middle">비밀번호확인&nbsp;<span class="star">*</span></td>
                            <td>
                                <input type="password" id="pwdcheck" maxlength="15" class="requiredInfo" />
                                <span class="error">암호가 일치하지 않습니다.</span>
                            </td>
                        </tr>

                        <!-- 이메일 입력 및 중복 확인 -->
                        <tr class="mb-3">
                            <td class="align-middle">이메일&nbsp;<span class="star">*</span></td>
                            <td>
                                <input type="text" name="email" id="email" maxlength="60" autocomplete="off" class="requiredInfo" />
                                <span class="error">이메일 형식에 맞지 않습니다.</span>
                                <span id="emailcheck">이메일중복확인</span>
                                <span id="emailCheckResult"></span>
                            </td>
                        </tr>

                        <!-- 전화번호 입력 -->
                        <tr class="mb-3">
                            <td class="align-middle">전화번호</td>
                            <td>
                                <input type="text" name="hp1" id="hp1" size="6" maxlength="3" value="010" readonly /> -
                                <input type="text" name="hp2" id="hp2" size="6" maxlength="4" /> -
                                <input type="text" name="hp3" id="hp3" size="6" maxlength="4" />
                                <span class="error">휴대폰 형식이 아닙니다.</span>
                            </td>
                        </tr>

                        <!-- 우편번호 검색 -->
                        <tr class="mb-3">
                            <td class="align-middle">우편번호</td>
                            <td>
                                <input type="text" name="postcode" id="postcode" size="6" maxlength="5" />
                                <img src="<%= ctxPath %>/images/b_zipcode.gif" id="zipcodeSearch" />
                                <span class="error">우편번호 형식에 맞지 않습니다.</span>
                            </td>
                        </tr>

                        <!-- 주소 입력 -->
                        <tr class="mb-3">
                            <td class="align-middle">주소</td>
                            <td>
                                <input type="text" name="address" id="address" size="40" maxlength="200" placeholder="주소" /><br>
                                <input type="text" name="detailaddress" id="detailAddress" size="40" maxlength="200" placeholder="상세주소" />
                                <input type="text" name="extraaddress" id="extraAddress" size="40" maxlength="200" placeholder="참고항목" />
                                <span class="error">주소를 입력하세요.</span>
                            </td>
                        </tr>

                        <!-- 생년월일 입력 -->
                        <tr class="mb-3">
                            <td class="align-middle">생년월일</td>
                            <td>
                                <input type="text" name="birthdate" id="datepicker" autocomplete="off" maxlength="10" />
                                <span class="error">생년월일은 마우스로만 클릭하세요.</span>
                            </td>
                        </tr>

                        <!-- 가입 및 취소 버튼 -->
                        <tr class="mb-3">
                            <td colspan="2" class="text-center">
                                <input type="button" class="btn btn-warning btn-lg mr-5" value="가입" onclick="goRegister()" />
                                <input type="reset" class="btn btn-warning btn-lg" value="취소" onclick="goReset()" />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
</div>
