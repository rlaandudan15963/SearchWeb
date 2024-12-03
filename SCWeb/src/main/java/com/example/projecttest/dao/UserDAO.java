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
  //전화번호 중복 체크(아래 수정이나 회원가입때 중복 체크로 사용할 예정)
    public boolean isPhoneNumberUnique(String phoneNum) {
      String sql = "SELECT COUNT(*) FROM WEB_USER WHERE USER_PhoneNum = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, phoneNum);
          try (ResultSet rs = stmt.executeQuery()) {
              if (rs.next()) {
                  return rs.getInt(1) == 0; // 중복되지 않으면 true
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
  }
    //회원 정보 수정하기위한 DB sql 업데이트 문(수정 내용 : 비밀번호, 이메일, 이름, 나이, 폰번호)
    public boolean updateUserDetails(String userId, String password, String email, String name, int age, String phoneNum) {
      String sql = "UPDATE WEB_USER SET USER_Password = ?, USER_Email = ?, USER_Name = ?, USER_Age = ?, USER_PhoneNum = ? WHERE USER_ID = ?";

      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, password); // 비밀번호 업데이트
          stmt.setString(2, email);
          stmt.setString(3, name);
          stmt.setInt(4, age);
          stmt.setString(5, phoneNum);
          stmt.setString(6, userId);

          int rowsUpdated = stmt.executeUpdate();
          return rowsUpdated > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
    // 친구 추가용(회원ID로 검색을 상정하고 작성)
    public boolean addFriend(String userId, String friendId) {
      String sql = "INSERT INTO FRIEND (USER_ID, FRIEND_ID, SHARE_IS) VALUES (?, ?, 0)";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, userId);
          stmt.setString(2, friendId);

          int rowsInserted = stmt.executeUpdate();
          return rowsInserted > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
    // 친구 중복 확인 (사용자 ID와 친구 ID 필요) 팀장 개인의 생각으론 어떻게 구현할진 모르지만 필요하면 쓰세요 
    public boolean isFriendUnique(String userId, String friendId) {
      String sql = "SELECT COUNT(*) FROM FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, userId);
          stmt.setString(2, friendId);

          try (ResultSet rs = stmt.executeQuery()) {
              if (rs.next()) {
                  return rs.getInt(1) == 0; // 0이면 중복되지 않음
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
  }
    //친구 삭제 기능(회원 ID, 친구 ID 
    public boolean removeFriend(String userId, String friendId) {
      String sql = "DELETE FROM FRIEND WHERE USER_ID = ? AND FRIEND_ID = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, userId);
          stmt.setString(2, friendId);
          int rowsDeleted = stmt.executeUpdate();
          return rowsDeleted > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
    //스케줄 수정 (model의 스케줄을 사용함)
    public boolean updateSchedule(Schedule schedule) {
      String sql = "UPDATE SCHEDULE SET TITLE = ?, DESCRIPTION = ?, START_DATE = ?, END_DATE = ? WHERE SCHEDULE_ID = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, schedule.getTitle());
          stmt.setString(2, schedule.getDescription());
          stmt.setString(3, schedule.getStartDate());
          stmt.setString(4, schedule.getEndDate());
          stmt.setInt(5, schedule.getScheduleId());

          int rowsUpdated = stmt.executeUpdate();
          return rowsUpdated > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
    //스케줄 소유자 확인을 위해 스케줄 ID로 스케줄을 가져오는 기능
    public Schedule getScheduleById(int scheduleId) {
      String sql = "SELECT * FROM SCHEDULE WHERE SCHEDULE_ID = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setInt(1, scheduleId);

          try (ResultSet rs = stmt.executeQuery()) {
              if (rs.next()) {
                  Schedule schedule = new Schedule();
                  schedule.setScheduleId(rs.getInt("SCHEDULE_ID"));
                  schedule.setUserId(rs.getString("USER_ID"));
                  schedule.setTitle(rs.getString("TITLE"));
                  schedule.setDescription(rs.getString("DESCRIPTION"));
                  schedule.setStartDate(rs.getString("START_DATE"));
                  schedule.setEndDate(rs.getString("END_DATE"));
                  return schedule;
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return null;
  }
    //스케줄 삭제 (스케줄ID를 사용함)
    public boolean deleteSchedule(int scheduleId) {
      String sql = "DELETE FROM SCHEDULE WHERE SCHEDULE_ID = ?";
      try (Connection conn = DBConnect.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setInt(1, scheduleId);

          int rowsDeleted = stmt.executeUpdate();
          return rowsDeleted > 0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
}