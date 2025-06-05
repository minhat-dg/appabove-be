package com.appabove.app.repository;

import com.appabove.app.model.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, String> {
    boolean existsByUser_IdAndAppName(String userId, String appName);

    List<App> findByUser_Id(String userId);

    Optional<App> findByAppId(String appId);
}