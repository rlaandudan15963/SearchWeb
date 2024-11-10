package com.example.projecttest.service;

import java.util.List;

import com.example.projecttest.dao.UserDAO;
import com.example.projecttest.model.Schedule;
import com.example.projecttest.model.User;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public boolean registerUser(String userId, String password, String email, String name, int age, String phoneNum) {
        // 여기서 비즈니스 로직을 추가할 수 있음 (예: 비밀번호 검증, 데이터 형식 확인 등)
        return userDAO.registerUser(userId, password, email, name, age, phoneNum);
    }
    
    public boolean validateUser(String username, String password) {
      return userDAO.checkUserCredentials(username, password);
  }
    
    public User getUserWithFriends(String userId) {
      return userDAO.getUserWithFriendsById(userId);
  }
    public boolean updateFriendShareStatus(String userId, String friendId, int shareStatus) {
      return userDAO.updateFriendShareStatus(userId, friendId, shareStatus);
  }
    public boolean addSchedule(String userId, String title, String description, String startDate, String endDate) {
      // 필요한 검증 로직을 추가할 수 있음
      if (userId == null || userId.isEmpty()) {
          return false; // 사용자 ID가 없으면 false 반환
      }
      return userDAO.addSchedule(userId, title, description, startDate, endDate);
  }
    public List<Schedule> getUserSchedules(String userId) {
      return userDAO.getSchedulesByUserId(userId);
  }
    public List<Schedule> getAllSchedules(String userId) {
      List<Schedule> userSchedules = getUserSchedules(userId);
      List<Schedule> friendSchedules = userDAO.getSharedFriendSchedules(userId);
      userSchedules.addAll(friendSchedules);
      return userSchedules;
  }
}