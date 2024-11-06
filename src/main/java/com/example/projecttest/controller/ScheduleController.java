package com.example.projecttest.controller;

import com.example.projecttest.model.Schedule;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final List<Schedule> schedules = new ArrayList<>();

    // 일정 추가 API
    @PostMapping("/add")
    public Schedule addSchedule(@RequestBody Schedule schedule) {
        schedules.add(schedule);
        return schedule;
    }

    // 일정 목록 조회 API
    @GetMapping
    public List<Schedule> getSchedules() {
        return schedules;
    }
}