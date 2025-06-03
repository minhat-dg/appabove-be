package com.appabove.app.repository;

import com.appabove.app.model.Build;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildRepository extends JpaRepository<Build, String> {
    List<Build> findByGroup_GroupId(String groupId, Sort sort);
}
