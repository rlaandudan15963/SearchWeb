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
        PreparedStatement pstmt = null;//SQL 문을 실행에 사용

        try {
            conn = DBconnect.getConnection();//DB연결클래스 함수 호출
            String sql = "INSERT INTO WEB_USER VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, username);
            pstmt.setString(5, userage);
            pstmt.setString(6, phonenum);

            int rowsInserted = pstmt.executeUpdate();//영향받은 행 반환(0이면 없다는 뜻)
            if (rowsInserted > 0) {
            	%>
            	            <script>
            	                alert("회원가입이 성공적으로 완료되었습니다!");
            	                window.location.href = "<%= request.getContextPath() %>/WebMain/Home.jsp";
            	            </script>
            	<%
            	        } else {
            	%>
            	            <script>
            	                alert("회원가입에 실패했습니다. 다시 시도해주세요.");
            	                window.location.href = "<%= request.getContextPath() %>/WebMain/Register.jsp";
            	            </script>
            	<%
            	        }
            	    } catch (SQLException e) {
            	%>
            	        <script>
            	            alert("오류 발생: <%= e.getMessage() %>");
            	            window.location.href = "<%= request.getContextPath() %>/WebMain/Register.jsp";
            	        </script>
            	<%
            	        e.printStackTrace();
            	    } finally {
            	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            	        if (conn != null) try { conn.close(); } catch (SQLException e) {}
            	    }
            	%>
</body>
</html>