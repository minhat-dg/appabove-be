package com.appabove.app.repository;

import com.appabove.app.model.App;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<App, String> {
    boolean existsByAppName(String appName);
}