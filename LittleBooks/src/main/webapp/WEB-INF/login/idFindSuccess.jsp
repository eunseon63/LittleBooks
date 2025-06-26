<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String userid = (String) request.getAttribute("userid");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>아이디 찾기 결과</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f9f9f9;
        }
        .result-box {
            text-align: center;
            background-color: #fff;
            padding: 40px 30px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-shadow: 2px 2px 10px rgba(0,0,0,0.05);
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .userid {
            font-size: 1.5rem;
            color: #007bff;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="result-box">
    <h2>아이디 찾기 결과</h2>
    <% if (!"존재하지 않습니다.".equals(userid)) { %>
        <div>찾으시는 아이디는 <span class="userid"><%= userid %></span> 입니다.</div>
    <% } else { %>
        <div style="color:red;">일치하는 정보가 없습니다.</div>
    <% } %>
</div>

</body>
</html>
