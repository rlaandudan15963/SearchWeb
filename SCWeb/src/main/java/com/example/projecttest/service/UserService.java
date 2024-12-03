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
 // 전화번호 중복 체크
    public boolean isPhoneNumberUnique(String phoneNum) {
        return userDAO.isPhoneNumberUnique(phoneNum);
    }

    //회원 수정 DAO와 연결하는 메서드(검증 같은 후처리가 필요하면 작성)
    public boolean updateUser(String userId, String password, String email, String name, int age, String phoneNum) {
      // 전화번호 중복 확인
      if (!isPhoneNumberUnique(phoneNum)) {
          throw new IllegalArgumentException("전화번호가 이미 사용 중입니다.");
      }
      // 중복이 없으면 업데이트 진행
      return userDAO.updateUserDetails(userId, password, email, name, age, phoneNum);
  }
    //친구 추가
    public boolean addFriend(String userId, String friendId) {
      // 사용자 자신을 친구로 추가하려고 하는 경우 예외 처리(이건 필요없으면 제거해도 됨)
      if (userId.equals(friendId)) {
          throw new IllegalArgumentException("자신을 친구로 추가할 수 없습니다.");
      }
      // 이미 존재하는 친구인지 확인 (중복 방지)
      if (!userDAO.isFriendUnique(userId, friendId)) {
          throw new IllegalArgumentException("이미 친구로 등록되어 있습니다.");
      }

      return userDAO.addFriend(userId, friendId);
    }
    public boolean removeFriend(String userId, String friendId) {
      return userDAO.removeFriend(userId, friendId);
  }
    //스케줄 업데이트
    public boolean updateSchedule(Schedule schedule) {
      // 필요한 경우 스케줄 소유자 확인 등의 추가 검증 로직을 포함할 수 있습니다.
      return userDAO.updateSchedule(schedule);
  }
    //스케줄 삭제
    public boolean deleteSchedule(int scheduleId, String userId) {
      // 스케줄 소유자 확인 로직 (스케줄이 해당 사용자의 것인지 확인)
      Schedule schedule = userDAO.getScheduleById(scheduleId);
      if (schedule == null) {
          throw new IllegalArgumentException("해당 스케줄이 존재하지 않습니다.");
      }
      if (!schedule.getUserId().equals(userId)) {
          throw new SecurityException("스케줄을 삭제할 권한이 없습니다.");
      }

      return userDAO.deleteSchedule(scheduleId);
  }
}