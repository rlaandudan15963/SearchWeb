package com.example.projecttest.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Schedule {
  private int scheduleId;
  private String userId;
  private String title;
  private String description;
  private String startDate;
  private String endDate;
  
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  // 기본 생성자
  public Schedule() {}

  // 모든 필드를 초기화하는 생성자
  public Schedule(String title, String description, String startDate, String endDate) {
      this.title = title;
      this.description = description;
      this.startDate = startDate;
      this.endDate = endDate;
  }
  public Schedule(String userId, String title, String description, String startDate, String endDate) {
    this.userId = userId;
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
}
//Getter and Setter for scheduleId
  public int getScheduleId() {
      return scheduleId;
  }

  public void setScheduleId(int scheduleId) {
      this.scheduleId = scheduleId;
  }
  // Getters and Setters
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getStartDate() { return startDate; }
  public void setStartDate(String startDate) { this.startDate = startDate; }

  public String getEndDate() { return endDate; }
  public void setEndDate(String endDate) { this.endDate = endDate; }
  
  public void setStartDateFromLocalDateTime(LocalDateTime startDateTime) {
    this.startDate = startDateTime.format(formatter);
  }

// Method to set endDate from LocalDateTime
  public void setEndDateFromLocalDateTime(LocalDateTime endDateTime) {
    this.endDate = endDateTime.format(formatter);
  }
}