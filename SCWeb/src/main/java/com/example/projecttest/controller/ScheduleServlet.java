package com.example.projecttest.controller;

import com.example.projecttest.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/addSchedule")
public class ScheduleServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ScheduleServlet: POST 요청 받음");

        // 요청 본문을 읽어 JSON으로 파싱
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String requestBody = sb.toString();
        System.out.println("Received JSON: " + requestBody);  // JSON 요청 내용 출력
        try {
        JSONObject jsonObject = new JSONObject(requestBody);
        String userId = jsonObject.getString("userId");
        String title = jsonObject.getString("title");
        String description = jsonObject.getString("description");
        String startDate = jsonObject.getString("startDate");
        String endDate = jsonObject.getString("endDate");

        // 스케줄을 DB에 저장
        boolean isSaved = userDAO.addSchedule(userId, title, description, startDate, endDate);

        // JSON 응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", isSaved);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
        }
    } catch (Exception e) {
      e.printStackTrace(); // 서버 로그에 오류 출력
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 에러 코드 반환
      response.setContentType("application/json;charset=UTF-8");
      JSONObject errorResponse = new JSONObject();
      errorResponse.put("success", false);
      errorResponse.put("error", e.getMessage());

      try (PrintWriter out = response.getWriter()) {
          out.print(errorResponse.toString());
      }
  }
}
}