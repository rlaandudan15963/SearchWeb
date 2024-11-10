package com.example.projecttest.controller;

import com.example.projecttest.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 요청의 Content-Type이 JSON인지 확인
        if ("application/json".equals(request.getContentType())) {
            // JSON 요청 본문을 읽음
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
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            // 사용자 인증 확인
            boolean isValidUser = userService.validateUser(username, password);
            if (isValidUser) {
              HttpSession session = request.getSession();
              session.setAttribute("userId", username); // 세션에 사용자명 저장
          }

            // JSON 응답 작성
            response.setContentType("application/json;charset=UTF-8");
            JSONObject responseJson = new JSONObject();
            responseJson.put("success", isValidUser);

            try (PrintWriter out = response.getWriter()) {
                out.print(responseJson.toString());
            }
        } else {
            // Content-Type이 JSON이 아닐 때의 에러 처리
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid Content-Type\"}");
        }
    }
}
