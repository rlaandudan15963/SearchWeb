<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.DBconnect" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 처리</title>
</head>
<body>
 <%
        String userID = request.getParameter("userID");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String userage = request.getParameter("userage");
        String phonenum = request.getParameter("phonenum");
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBconnect.getConnection();
            String sql = "INSERT INTO WEB_USER VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, username);
            pstmt.setString(5, userage);
            pstmt.setString(6, phonenum);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                out.println("<h2>회원가입이 성공적으로 완료되었습니다!</h2>");
            } else {
                out.println("<h2>회원가입에 실패했습니다. 다시 시도해주세요.</h2>");
            }
        } catch (SQLException e) {
            out.println("<h2>오류 발생: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    %>
    <a href="${pageContext.request.contextPath}/WebMain/Register.jsp">다시 등록하기</a>
</body>
</html>