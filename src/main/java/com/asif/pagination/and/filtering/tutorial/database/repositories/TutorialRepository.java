package com.asif.pagination.and.filtering.tutorial.database.repositories;

import com.asif.pagination.and.filtering.tutorial.database.entities.Tutorial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TutorialRepository extends JpaRepository<Tutorial, Integer> {
    Page<Tutorial> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    @Modifying
    @Query("update Tutorial t set t.published = :published where t.id = :id")
    public void updatePublishedStatus(Integer id, boolean published);
}
