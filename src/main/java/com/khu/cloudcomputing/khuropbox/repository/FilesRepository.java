package com.khu.cloudcomputing.khuropbox.repository;

import com.khu.cloudcomputing.khuropbox.entity.Files;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FilesRepository extends JpaRepository<Files, Integer> {
    List<Files> findAll();
    Optional<Files> findById(Integer id);
    void deleteById(Integer id);
}
