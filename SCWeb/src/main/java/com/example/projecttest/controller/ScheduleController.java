package com.example.projecttest.controller;

import com.example.projecttest.model.Schedule;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/schedule")
public class ScheduleController extends HttpServlet {
    private final List<Schedule> schedules = new ArrayList<>();

    // 일정 추가
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 클라이언트가 보내는 데이터를 Schedule 객체로 변환
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // Schedule 객체 생성 및 리스트에 추가
        Schedule schedule = new Schedule(title, description, startDate, endDate);
        schedules.add(schedule);

        // JSON 형식으로 응답
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"일정이 추가되었습니다.\", \"title\":\"" + title + "\"}");
    }

    // 일정 목록 조회
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        StringBuilder jsonOutput = new StringBuilder("[");

        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            jsonOutput.append("{\"title\":\"").append(schedule.getTitle()).append("\",")
                      .append("\"description\":\"").append(schedule.getDescription()).append("\",")
                      .append("\"startDate\":\"").append(schedule.getStartDate()).append("\",")
                      .append("\"endDate\":\"").append(schedule.getEndDate()).append("\"}");

            if (i < schedules.size() - 1) jsonOutput.append(",");
        }
        jsonOutput.append("]");

        response.getWriter().write(jsonOutput.toString());
    }
}