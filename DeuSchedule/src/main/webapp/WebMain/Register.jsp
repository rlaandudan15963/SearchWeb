<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<body>
<h2>회원가입</h2>
    <form action="${pageContext.request.contextPath}/WebMain/RegisterCheck.jsp" method="post" accept-charset="UTF-8">
        <label for="userID">아이디:</label>
        <input type="text" id="userID" name="userID" required><br><br>
        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required><br><br>
        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" required><br><br>
        <label for="username">이름:</label>
        <input type="text" id="username" name="username" required><br><br>
        <label for="userage">나이:</label>
        <input type="text" id="userage" name="userage" required><br><br>
        <label for="phonenum">전화번호:</label>
        <input type="text" id="phonenum" name="phonenum" required><br><br>
        <input type="submit" value="가입하기">
    </form>
</body>
</html>