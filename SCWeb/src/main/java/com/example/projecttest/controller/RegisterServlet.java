package com.example.projecttest.controller;

import com.example.projecttest.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import org.json.JSONObject;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 요청의 Content-Type이 JSON인지 확인
        if ("application/json".equals(request.getContentType())) {
            // 요청의 본문을 읽기
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // JSON 파싱
            String requestBody = sb.toString();
            JSONObject jsonObject = new JSONObject(requestBody);

            // JSON 데이터에서 필드 추출
            String userId = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("name");
            int age = jsonObject.getInt("age");
            String phoneNum = jsonObject.getString("phone");

            // 서비스 클래스를 통해 사용자 등록 처리
            boolean isRegistered = userService.registerUser(userId, password, email, name, age, phoneNum);

            // 응답 JSON 작성
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\": " + isRegistered + "}");
        } else {
            // Content-Type이 JSON이 아닌 경우 처리
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid Content-Type\"}");
        }
    }
}