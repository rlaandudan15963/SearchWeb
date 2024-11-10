package com.example.projecttest;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("애플리케이션 초기화 시작...");
        // 초기화 작업 예: 데이터베이스 연결 설정 등
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("애플리케이션 종료...");
        // 종료 작업 예: 데이터베이스 연결 해제 등
    }
}