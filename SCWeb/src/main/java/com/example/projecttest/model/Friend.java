package com.example.projecttest.model;

public class Friend {
    private String userId;      // 사용자 아이디
    private String friendId;     // 친구 아이디
    private int shareStatus;     // 공유 여부 (0: 공유 안함, 1: 공유함)

    // 기본 생성자
    public Friend() {}
    public Friend(String friendId, int shareStatus) {
      this.friendId = friendId;
      this.shareStatus = shareStatus;
  }
    // 모든 필드를 사용하는 생성자
    public Friend(String userId, String friendId, int shareStatus) {
        this.userId = userId;
        this.friendId = friendId;
        this.shareStatus = shareStatus;
    }

    // Getter 및 Setter 메서드
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public int getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(int shareStatus) {
        this.shareStatus = shareStatus;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userId='" + userId + '\'' +
                ", friendId='" + friendId + '\'' +
                ", shareStatus=" + shareStatus +
                '}';
    }
}