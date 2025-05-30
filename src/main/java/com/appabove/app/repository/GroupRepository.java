package com.appabove.app.repository;

import com.appabove.app.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, String> {
    boolean existsByGroupName(String groupName);
}
