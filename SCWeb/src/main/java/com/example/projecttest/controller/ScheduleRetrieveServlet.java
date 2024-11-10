package com.example.projecttest.controller;

import com.example.projecttest.model.Schedule;
import com.example.projecttest.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/loadSchedules")
public class ScheduleRetrieveServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession(false);
      String userId = (session != null) ? (String) session.getAttribute("userId") : null;

      response.setContentType("application/json;charset=UTF-8");
      JSONObject jsonResponse = new JSONObject();

        if (userId != null) {
            List<Schedule> schedules = userService.getAllSchedules(userId);

            JSONArray scheduleArray = new JSONArray();
            for (Schedule schedule : schedules) {
                JSONObject scheduleJson = new JSONObject();
                scheduleJson.put("title", schedule.getTitle());
                scheduleJson.put("description", schedule.getDescription());
                scheduleJson.put("startDate", schedule.getStartDate());
                scheduleJson.put("endDate", schedule.getEndDate());
                scheduleJson.put("userId", schedule.getUserId()); // 사용자 구분용
                scheduleArray.put(scheduleJson);
            }
            jsonResponse.put("schedules", scheduleArray);
            jsonResponse.put("success", true);
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "User not logged in");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
        }
    }
}