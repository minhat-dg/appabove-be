package com.appabove.app.repository;

import com.appabove.app.model.UploadedFile;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUploadRepository extends JpaRepository<UploadedFile, String> {
    List<UploadedFile> findByGroup_GroupId(String groupId, Sort sort);
}
