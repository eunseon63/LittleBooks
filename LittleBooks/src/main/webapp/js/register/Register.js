// 플래그: 중복확인 여부 확인
let idChecked = false;
let emailChecked = false;

$(function() {
  // 초기 설정
  $('span.error').hide();
  $('#name').focus();
  $('#postcode, #address, #extraAddress').attr('readonly', true);

  // 이벤트 바인딩 - 필드별 유효성 검사
  $('#name').blur(validateName);
  $('#userid').blur(validateUserId);
  $('#pwd').blur(validatePassword);
  $('#pwdcheck').blur(validatePasswordConfirm);
  $('#email').blur(validateEmail);
  $('#hp2').blur(() => checkPattern('#hp2', /^[1-9]\d{3}$/, '국번 4자리 입력하세요.'));
  $('#hp3').blur(() => checkPattern('#hp3', /^\d{4}$/, '번호 4자리 입력하세요.'));
  $('#postcode').blur(validatePostcode);

  // 우편번호 찾기 & datepicker 초기화
  $('#zipcodeSearch').click(openPostcode);
  initDatepicker();

  // 중복확인 버튼 클릭 처리
  $('#idcheck').click(() => checkDuplicate('userid'));
  $('#userid').change(() => idChecked = false);

  $('#emailcheck').click(() => checkDuplicate('email'));
  $('#email').change(() => emailChecked = false);
});

/**
 * 에러 메시지 표시/숨김 함수
 * @param {string} selector - input 요소 선택자
 * @param {string} message - 에러 메시지 (없으면 숨김)
 * @returns {boolean} - 에러 여부 (true = 정상)
 */
function toggleError(selector, message = '') {
  const $errorSpan = $(selector).siblings('span.error');
  if (message) {
    $errorSpan.text(message).show();
    return false;
  } else {
    $errorSpan.hide();
    return true;
  }
}

/**
 * 필수 입력 체크
 * @param {string} selector - input 요소 선택자
 * @param {string} fieldName - 필드 이름 (메시지용)
 * @returns {boolean}
 */
function checkRequired(selector, fieldName) {
  const value = $(selector).val().trim();
  if (!value) {
    return toggleError(selector, `${fieldName}을(를) 입력하세요.`);
  }
  return toggleError(selector);
}

/**
 * 정규식 검사
 * @param {string} selector - input 요소 선택자
 * @param {RegExp} regex - 정규식 객체
 * @param {string} errorMsg - 에러 메시지
 * @returns {boolean}
 */
function checkPattern(selector, regex, errorMsg) {
  const value = $(selector).val().trim();
  if (!regex.test(value)) {
    return toggleError(selector, errorMsg);
  }
  return toggleError(selector);
}

/**
 * 비밀번호 확인 검사
 * @param {string} pwdSelector - 비밀번호 input 선택자
 * @param {string} confirmSelector - 비밀번호 확인 input 선택자
 * @returns {boolean}
 */
function checkPasswordMatch(pwdSelector, confirmSelector) {
  const pwd = $(pwdSelector).val();
  const confirm = $(confirmSelector).val();
  if (pwd !== confirm) {
    toggleError(confirmSelector, '비밀번호가 일치하지 않습니다.');
    $(pwdSelector).val('');
    $(confirmSelector).val('');
    $(pwdSelector).focus();
    return false;
  }
  return toggleError(confirmSelector);
}

// 개별 필드 유효성 검사 함수들
function validateName() {
  return checkRequired('#name', '이름');
}

function validateUserId() {
	 if ($('#idcheckResult').text() === '이미 사용 중입니다.') {
    return true; // 이미 메시지 출력했으므로 중복 메시지 방지
  }
  return checkRequired('#userid', '아이디');
}

function validatePassword() {
  const regex = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/;
   
  return checkPattern('#pwd', regex, '비밀번호는 영문자+숫자+특수문자 포함 8~15자입니다.');
}

function validatePasswordConfirm() {
  return checkPasswordMatch('#pwd', '#pwdcheck');
}

function validateEmail() {
  const regex = /^[\w.-]+@([\w-]+\.)+[A-Za-z]{2,3}$/;
  if ($('#emailCheckResult').text() === '이미 사용 중입니다.') {
    return true; // 이미 메시지 출력했으므로 중복 메시지 방지
  }
  return checkPattern('#email', regex, '유효한 이메일 형식이 아닙니다.');
}

function validatePhone() {
  const hp2Valid = checkPattern('#hp2', /^[1-9]\d{3}$/, '국번 4자리 입력하세요.');
  const hp3Valid = checkPattern('#hp3', /^\d{4}$/, '번호 4자리 입력하세요.');
  return hp2Valid && hp3Valid;
}

function validatePostcode() {
  return checkPattern('#postcode', /^\d{5}$/, '우편번호는 5자리 숫자입니다.');
}

function validateAddress() {
  const addr1 = $('#address').val().trim();
  const addr2 = $('#detailAddress').val().trim();
  if (!addr1 || !addr2) {
    alert('주소를 모두 입력하세요.');
    return false;
  }
  return true;
}

function validateBirthdate() {
  const val = $('#datepicker').val().trim();
  if (!val) {
    alert('생년월일을 선택해 주세요.');
    return false;
  }
  return true;
}

/**
 * 중복 확인 AJAX 요청
 * @param {string} type - 'userid' 또는 'email'
 */
function checkDuplicate(type) {
  const sel = `#${type}`;
  const val = $(sel).val().trim();
  if (!val) {
    alert(`${type === 'userid' ? '아이디' : '이메일'}를 입력하세요.`);
    return;
  }

  let url = '';
  if(type === 'userid') {
    url = `${ctxPath}/register/idDuplicateCheck.go`;  // 여기서 정확한 매핑 URL로 맞춤
  } else if(type === 'email') {
    url = `${ctxPath}/register/emailDuplicateCheck.go`;
  }

  $.post(
    url,
    { [type]: val },
    type === 'email' ? handleEmailCheck : handleIdCheck,
    'json'  // 서버에서 항상 JSON을 응답하므로 타입을 통일하는 게 좋음
  );
}

function handleIdCheck(json) {
  if (json.isExists) {
    $('#idcheckResult').text('이미 사용 중입니다.').css('color', 'red');

    // 중복된 아이디일 경우: blur 이벤트 제거 → 값 비우기 → 포커스 → blur 이벤트 재등록
    $('#userid').off('blur');
    $('#userid').val('');
    idChecked = false;

    // blur 이벤트 재등록 (validateUserId가 자동 실행되지 않도록 시간차)
    setTimeout(() => {
      $('#userid').on('blur', validateUserId);
    }, 300);

    $('#userid').focus(); // 마지막에 포커스를 주어 사용자 입력 유도
  } else {
    $('#idcheckResult').text('사용 가능한 아이디입니다.').css('color', 'navy');
    idChecked = true;
  }
}

function handleEmailCheck(json) {
  if (json.isExists) {
    $('#emailCheckResult').text('이미 사용 중입니다.').css('color', 'red');

    $('#email').off('blur');
    $('#email').val('');
    emailChecked = false;

    setTimeout(() => {
      $('#email').on('blur', validateEmail);
    }, 300);

    $('#email').focus();
  } else {
    $('#emailCheckResult').text('사용 가능한 이메일입니다.').css('color', 'navy');
    emailChecked = true;
  }
}

/**
 * Daum postcode API 사용해서 우편번호 찾기
 */
function openPostcode() {
  new daum.Postcode({
    oncomplete: data => {
      const addr = data.userSelectedType === 'R'
        ? data.roadAddress
        : data.jibunAddress;

      let extra = '';
      if (data.userSelectedType === 'R' && data.bname && /[동|로|가]$/.test(data.bname)) {
        extra = data.bname;
      }
      if (data.buildingName && data.apartment === 'Y') {
        extra += (extra ? `, ${data.buildingName}` : data.buildingName);
      }

      $('#extraAddress').val(extra ? `(${extra})` : '');
      $('#postcode').val(data.zonecode);
      $('#address').val(addr);
      $('#detailAddress').focus();
    }
  }).open();
}

/**
 * 생년월일 datepicker 초기화
 */
function initDatepicker() {
  $('#datepicker').datepicker({
    dateFormat: 'yy-mm-dd',
    showOtherMonths: true,
    showMonthAfterYear: true,
    changeYear: true,
    changeMonth: true,
    yearSuffix: '년',
    monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'],
    monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
    dayNamesMin: ['일','월','화','수','목','금','토'],
    dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일']
  }).on('change', function() {
    toggleError('#datepicker'); // 에러 숨기기
  }).on('keyup', function() {
    toggleError('#datepicker', '달력으로 선택해 주세요.');
    $(this).val('');
  });
}

/**
 * 가입하기 버튼 클릭 시 전체 검증 및 제출
 */
function goRegister() {
  if (!validateName()) return;
  if (!validateUserId()) return;
  if (!validatePassword()) return;
  if (!validatePasswordConfirm()) return;
  if (!validateEmail()) return;
  if (!validatePhone()) return;
  if (!validatePostcode()) return;

  if (!idChecked) {
    alert('아이디 중복확인을 먼저 해주세요.');
    return;
  }
  if (!emailChecked) {
    alert('이메일 중복확인을 먼저 해주세요.');
    return;
  }

  if (!validateAddress()) return;
  if (!validateBirthdate()) return;

  // 모든 검증 통과 시 폼 제출
  document.registerFrm.method = 'post';
  document.registerFrm.submit();
}

/**
 * 폼 초기화 함수 (필요 시 사용)
 */
function goReset() {
  document.registerFrm.reset();
  $('span.error').hide();
  $('#idcheckResult, #emailCheckResult').text('');
  idChecked = false;
  emailChecked = false;
  $('#name').focus();
}
