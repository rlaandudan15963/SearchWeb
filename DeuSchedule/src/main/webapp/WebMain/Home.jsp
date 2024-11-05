<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
<style>
    /* 간단한 레이아웃을 위한 스타일 */
    #container {
        display: flex;
        justify-content: space-between;
        width: 80%;
        margin: 0 auto;
        margin-top: 20px;
    }
    #schedule, #friends {
        width: 48%;
        border: 1px solid #ccc;
        padding: 10px;
        box-sizing: border-box;
    }
    #auth {
        text-align: right;
        margin-top: 20px;
    }
    button {
        padding: 5px 10px;
        margin-left: 5px;
    }
</style>
</head>
<body>

<div id="auth">
    <button onclick="location.href='<%= request.getContextPath() %>/WebMain/Login.jsp'">로그인</button>
    <button onclick="location.href='<%= request.getContextPath() %>/WebMain/Register.jsp'">회원가입</button>
</div>

<h1>메인 페이지</h1>

<div id="container">
    <div id="schedule">
        <h2>이번 주 일정 (24시간)</h2>
        <table>
            <tr>
                <th>시간</th>
                <th>일</th>
                <th>월</th>
                <th>화</th>
                <th>수</th>
                <th>목</th>
                <th>금</th>
                <th>토</th>
            </tr>
            <% 
                for (int hour = 0; hour < 24; hour++) {
            %>
            <tr>
                <td><%= hour %>시</td>
                <% for (int day = 0; day < 7; day++) { %>
                    <td></td>
                <% } %>
            </tr>
            <% 
                }
            %>
        </table>
    </div>

    <div id="friends">
        <h2>친구 목록</h2>
        <ul>
            <li>친구1</li>
            <li>친구2</li>
            <li>친구3</li>
            <!-- 임시로 친구 목록 표시 -->
        </ul>
    </div>
</div>
</body>
</html>