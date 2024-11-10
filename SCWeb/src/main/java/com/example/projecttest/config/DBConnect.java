package com.example.projecttest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:xe";//DB 주소 ip주소에는 보통 localhost라 되어 있을 것 
            String user = "DEUWEB";//DB에 접속할 유저 이름
            String password = "1234";//비밀번호

            Class.forName("oracle.jdbc.OracleDriver");//오라클 드라이버 불러오기
            System.out.println("JDBC 드라이버 로드 성공");
            conn = DriverManager.getConnection(url, user, password);//연결 실행
            System.out.println("DB 연결 시도 성공");
        } catch (ClassNotFoundException e) {
          System.out.println("JDBC 드라이버 로드 실패: " + e.getMessage());
          e.printStackTrace();
      } catch (SQLException e) {
          System.out.println("DB 연결 실패: " + e.getMessage());
          e.printStackTrace();
      }
        return conn;//연결된 객체를 반환함 이걸로 DB 수행
    }
}