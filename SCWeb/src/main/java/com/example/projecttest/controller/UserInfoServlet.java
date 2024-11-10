package com.example.projecttest.controller;

import com.example.projecttest.model.User;
import com.example.projecttest.model.Friend;
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

@WebServlet("/userInfo")
public class UserInfoServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpSession session = request.getSession(false);
      String userId = (session != null) ? (String) session.getAttribute("userId") : null;
        // 사용자 정보 및 친구 목록 가져오기
      response.setContentType("application/json;charset=UTF-8");
      JSONObject jsonResponse = new JSONObject();
      try (PrintWriter out = response.getWriter()) {
        if (userId != null) {
            // 사용자 정보 및 친구 목록 가져오기
            User user = userService.getUserWithFriends(userId);

            if (user != null) {
                // 사용자 정보 JSON 변환
                JSONObject userJson = new JSONObject();
                userJson.put("userId", user.getUserId());
                userJson.put("email", user.getEmail());
                userJson.put("name", user.getName());
                userJson.put("age", user.getAge());
                userJson.put("phoneNum", user.getPhoneNum());
                jsonResponse.put("user", userJson);

                // 친구 목록 JSON 배열로 변환
                JSONArray friendsArray = new JSONArray();
                List<Friend> friends = user.getFriends();
                for (Friend friend : friends) {
                    JSONObject friendJson = new JSONObject();
                    friendJson.put("friendId", friend.getFriendId());
                    friendJson.put("shareStatus", friend.getShareStatus());
                    friendsArray.put(friendJson);
                }
                jsonResponse.put("friends", friendsArray);
                jsonResponse.put("success", true);
            } else {
                jsonResponse.put("success", false);
                jsonResponse.put("error", "User not found");
            }
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("error", "User is not logged in");
        }

        out.print(jsonResponse.toString());
        }
    }
}