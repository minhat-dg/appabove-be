package com.appabove.app.repository;

import com.appabove.app.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, String> {
    boolean existsByApp_AppIdAndGroupName(String appId, String groupName);
    List<Group> findByApp_AppId(String appId);
}
