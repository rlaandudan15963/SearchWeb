package com.example.projecttest.controller;

import com.example.projecttest.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

@WebServlet("/toggleFriendShare")
public class FriendShareToggleServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // JSON 파싱
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        JSONObject jsonRequest = new JSONObject(sb.toString());
        String userId = jsonRequest.getString("userId");
        String friendId = jsonRequest.getString("friendId");
        int shareStatus = jsonRequest.getInt("shareStatus");
        
        // 현재 공유 상태를 반전합니다
        int newShareStatus = (shareStatus == 0) ? 1 : 0;

        System.out.println("Received data - User ID: " + userId + ", Friend ID: " + friendId + ", Share Status: " + newShareStatus);

        boolean success = userService.updateFriendShareStatus(userId, friendId, newShareStatus);

        // 응답 생성
        response.setContentType("application/json;charset=UTF-8");
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);

        if (success) {
            System.out.println("Share status updated successfully.");
        } else {
            System.out.println("Failed to update share status.");
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
        }
    }
}