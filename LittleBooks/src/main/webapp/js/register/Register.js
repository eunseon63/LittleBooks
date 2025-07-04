// 플래그: 중복확인 여부 확인
let idChecked = false;
let emailChecked = false;

$(function() {
  // 초기 설정
  $('span.error').hide();
  $('#name').focus();
  $('#postcode, #address, #extraAddress').attr('readonly', true);

  // 유효성검사용 blur 이벤트 등록
  $('#name').blur(() => validateRequired('#name', '이름을 입력하세요.'));
  $('#userid').blur(() => validateRequired('#userid', '아이디를 입력하세요.'));
  $('#pwd').blur(() => validateRegex('#pwd', /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/,
    '비밀번호는 영문자+숫자+특수문자 포함 8~15자입니다.'));
  $('#pwdcheck').blur(validatePasswordConfirm);
  $('#email').blur(() => validateRegex('#email', /^[\w.-]+@([\w-]+\.)+[A-Za-z]{2,3}$/,
    '유효한 이메일 형식이 아닙니다.'));
  $('#hp2').blur(() => validateRegex('#hp2', /^[1-9]\d{3}$/, '국번 4자리 입력하세요.'));
  $('#hp3').blur(() => validateRegex('#hp3', /^\d{4}$/, '번호 4자리 입력하세요.'));
  $('#postcode').blur(() => validateRegex('#postcode', /^\d{5}$/, '우편번호는 5자리 숫자입니다.'));

  // 우편번호 찾기 & datepicker 초기화
  $('#zipcodeSearch').click(openPostcode);
  initDatepicker();

  // 중복확인 버튼 클릭 처리
  $('#idcheck').click(checkDuplicate.bind(null, 'userid'));
  $('#userid').change(() => idChecked = false);

  $('#emailcheck').click(checkDuplicate.bind(null, 'email'));
  $('#email').change(() => emailChecked = false);
});

/** 필수 필드 입력 확인 */
function validateRequired(selector, msg) {
  const val = $(selector).val().trim();
  return showErrorIf(!val, selector, msg);
}

/** 정규식 체크 */
function validateRegex(selector, regex, msg) {
  const ok = regex.test($(selector).val().trim());
  return showErrorIf(!ok, selector, msg);
}

/** 에러 메시지 표시 처리 */
function showErrorIf(show, selector, msg) {
  const span = $(selector).siblings('span.error');
  if (show) {
    span.text(msg).show();
    return false;
  }
  span.hide();
  return true;
}

/** 비밀번호 확인 */
function validatePasswordConfirm() {
  const pwd = $('#pwd').val(), confirm = $('#pwdcheck').val();
  if (pwd !== confirm) {
    showErrorIf(true, '#pwdcheck', '비밀번호가 일치하지 않습니다.');
    $('#pwd, #pwdcheck').val('');
    $('#pwd').focus();
    return false;
  }
  showErrorIf(false, '#pwdcheck');
  return true;
}

/** 중복 확인 AJAX 요청 */
function checkDuplicate(type) {
  const sel = `#${type}`, val = $(sel).val().trim();
  if (!val) {
    alert(`${type === 'userid' ? '아이디' : '이메일'}를 입력하세요.`);
    return;
  }
  $.post(
    `${type}DuplicateCheck.go`,
    { [type]: val },
    type === 'email' ? handleEmailCheck : handleIdCheck,
    type === 'email' ? 'json' : 'text'
  );
}

function handleIdCheck(res) {
  const json = JSON.parse(res);
  idChecked = true;
  if (json.isExists) {
    $('#idcheckResult').text('이미 사용 중입니다.').css('color', 'red');
    $('#userid').val('').focus();
    idChecked = false;
  } else {
    $('#idcheckResult').text('사용 가능한 아이디입니다.').css('color', 'navy');
  }
}

function handleEmailCheck(json) {
  emailChecked = true;
  if (json.isExists) {
    $('#emailCheckResult').text('이미 사용 중입니다.').css('color', 'red');
    $('#email').val('').focus();
    emailChecked = false;
  } else {
    $('#emailCheckResult').text('사용 가능한 이메일입니다.').css('color', 'navy');
  }
}

/** Daum postcode API */
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

/** 생년월일 datepicker 초기화 */
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
    showErrorIf(false, '#datepicker');
  }).on('keyup', function() {
    showErrorIf(true, '#datepicker', '달력으로 선택해 주세요.');
    $(this).val('');
  });
}

/** 가입하기 버튼 클릭 */
function goRegister() {
  // 필수 입력 검사
  if (!validateRequired('#name', '이름을 입력하세요.')) return;
  if (!validateRequired('#userid', '아이디를 입력하세요.')) return;
  if (!validateRegex('#pwd', /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/, 
        '비밀번호는 영문자+숫자+특수문자 포함 8~15자입니다.')) return;
  if (!validatePasswordConfirm()) return;
  if (!validateRegex('#email', /^[\w.-]+@([\w-]+\.)+[A-Za-z]{2,3}$/, 
        '유효한 이메일 형식이 아닙니다.')) return;
  if (!validateRegex('#hp2', /^[1-9]\d{3}$/, '국번 4자리 입력하세요.')) return;
  if (!validateRegex('#hp3', /^\d{4}$/, '번호 4자리 입력하세요.')) return;
  if (!validateRegex('#postcode', /^\d{5}$/, '우편번호는 5자리 숫자입니다.')) return;

  // 중복확인 플래그 체크
  if (!idChecked) return alert('아이디 중복확인을 먼저 해주세요.');
  if (!emailChecked) return alert('이메일 중복확인을 먼저 해주세요.');

  // 주소 및 생년월일 체크
  if (!$('#address').val().trim() || !$('#detailAddress').val().trim()) {
    return alert('주소를 모두 입력하세요.');
  }
  if (!$('#datepicker').val().trim()) {
    return alert('생년월일을 선택해 주세요.');
  }

  // submit
  document.registerFrm.method = 'post';
  document.registerFrm.submit();
}
