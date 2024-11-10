package com.example.projecttest.dao;

import com.example.projecttest.config.DBConnect;
import com.example.projecttest.model.User;
import com.example.projecttest.model.Friend;
import com.example.projecttest.model.Schedule;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class UserDAO {
  
    public boolean registerUser(String userId, String password, String email, String name, int age, String phoneNum) {
        String sql = "INSERT INTO WEB_USER (USER_ID, USER_Password, USER_Email, USER_Name, USER_Age, USER_PhoneNum) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection(); 
              PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, name);
            stmt.setInt(5, age);
            stmt.setString(6, phoneNum);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean checkUserCredentials(String username, String password) {
      String sql = "SELECT 1 FROM WEB_USER WHERE USER_ID = ? AND USER_Password = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, username);
          stmt.setString(2, password);

          try (ResultSet rs = stmt.executeQuery()) {
              return rs.next(); // 결과가 있으면 로그인 정보가 일치함을 의미
          }
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
    public User getUserWithFriendsById(String userId) {
      String userQuery = "SELECT USER_ID, USER_Email, USER_Name, USER_Age, USER_PhoneNum FROM WEB_USER WHERE USER_ID = ?";
      String friendQuery = "SELECT FRIEND_ID, SHARE_IS FROM FRIEND WHERE USER_ID = ?";
      User user = null;

      try (Connection conn = DBConnect.getConnection()) {
          // 사용자 정보 조회
          try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
              userStmt.setString(1, userId);
              ResultSet userRs = userStmt.executeQuery();
              if (userRs.next()) {
                  user = new User();
                  user.setUserId(userRs.getString("USER_ID"));
                  user.setEmail(userRs.getString("USER_Email"));
                  user.setName(userRs.getString("USER_Name"));
                  user.setAge(userRs.getInt("USER_Age"));
                  user.setPhoneNum(userRs.getString("USER_PhoneNum"));
              }
          }

          // 친구 정보 조회
          if (user != null) {
              List<Friend> friends = new ArrayList<>();
              try (PreparedStatement friendStmt = conn.prepareStatement(friendQuery)) {
                  friendStmt.setString(1, userId);
                  ResultSet friendRs = friendStmt.executeQuery();
                  while (friendRs.next()) {
                      Friend friend = new Friend();
                      friend.setUserId(userId);
                      friend.setFriendId(friendRs.getString("FRIEND_ID"));
                      friend.setShareStatus(friendRs.getInt("SHARE_IS"));
                      friends.add(friend);
                  }
              }
              user.setFriends(friends); // User 객체에 친구 목록 설정
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return user; // 사용자 정보와 친구 목록이 설정된 User 객체 반환
  }
    public boolean updateFriendShareStatus(String userId, String friendId, int shareStatus) {
      String sql = "UPDATE FRIEND SET SHARE_IS = ? WHERE USER_ID = ? AND FRIEND_ID = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setInt(1, shareStatus);
          stmt.setString(2, userId);
          stmt.setString(3, friendId);

          int rowsAffected = stmt.executeUpdate();
          System.out.println("Rows affected: " + rowsAffected);
          return rowsAffected > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
    public boolean addSchedule(String userId, String title, String description, String startDate, String endDate) {
      String sql = "INSERT INTO SCHEDULE (SCHEDULE_ID, USER_ID, TITLE, DESCRIPTION, START_DATE, END_DATE) VALUES (SCHEDULE_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);
        
        stmt.setString(1, userId);
        stmt.setString(2, title);
        stmt.setString(3, description);
        stmt.setTimestamp(4, Timestamp.valueOf(startDateTime));
        stmt.setTimestamp(5, Timestamp.valueOf(endDateTime));

          int rowsInserted = stmt.executeUpdate();
          System.out.println("DB 삽입 결과: " + rowsInserted);
          return rowsInserted > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
    public List<Schedule> getSchedulesByUserId(String userId) {
      String sql = "SELECT USER_ID, TITLE, DESCRIPTION, START_DATE, END_DATE FROM SCHEDULE WHERE USER_ID = ?";
      List<Schedule> schedules = new ArrayList<>();

      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          
          stmt.setString(1, userId);
          try (ResultSet rs = stmt.executeQuery()) {
              while (rs.next()) {
                  Schedule schedule = new Schedule();
                  schedule.setUserId(rs.getString("USER_ID"));
                  schedule.setTitle(rs.getString("TITLE"));
                  schedule.setDescription(rs.getString("DESCRIPTION"));
                  schedule.setStartDateFromLocalDateTime(rs.getTimestamp("START_DATE").toLocalDateTime());
                  schedule.setEndDateFromLocalDateTime(rs.getTimestamp("END_DATE").toLocalDateTime());
                  schedules.add(schedule);
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return schedules;
  }
    public List<Schedule> getSharedFriendSchedules(String userId) {
      List<Schedule> schedules = new ArrayList<>();
      String sql = "SELECT s.SCHEDULE_ID, s.USER_ID, s.TITLE, s.DESCRIPTION, s.START_DATE, s.END_DATE " +
                   "FROM SCHEDULE s " +
                   "JOIN FRIEND f ON s.USER_ID = f.FRIEND_ID " +
                   "WHERE f.USER_ID = ? AND f.SHARE_IS = 1";

      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, userId);
          ResultSet rs = stmt.executeQuery();

          while (rs.next()) {
              Schedule schedule = new Schedule();
              schedule.setScheduleId(rs.getInt("SCHEDULE_ID"));
              schedule.setUserId(rs.getString("USER_ID"));
              schedule.setTitle(rs.getString("TITLE"));
              schedule.setDescription(rs.getString("DESCRIPTION"));
              schedule.setStartDate(rs.getString("START_DATE"));
              schedule.setEndDate(rs.getString("END_DATE"));
              schedules.add(schedule);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return schedules;
  }
}