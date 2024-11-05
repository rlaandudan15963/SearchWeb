package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {
	//DB 접속 함수
	public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            String url = "jdbc:oracle:thin:@localhost:1521:xe";//오라클 주소(localhost를 DB실행한 컴 ip로 설정)
            String user = "DEUWEB";//연결할 DB이름
            String password = "1234";//비번

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
