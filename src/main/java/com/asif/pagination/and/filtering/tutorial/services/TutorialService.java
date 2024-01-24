package com.asif.pagination.and.filtering.tutorial.services;

import com.asif.pagination.and.filtering.tutorial.database.entities.Tutorial;
import com.asif.pagination.and.filtering.tutorial.database.repositories.TutorialRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class TutorialService {
    private final TutorialRepository tutorialRepository;

    public Page<Tutorial> findAll(String[] sort, int page, int size) {
        Pageable pageable = createPageable(sort, page, size);

        Page<Tutorial> tutorialPage;
        tutorialPage = tutorialRepository.findAll(pageable);
        return tutorialPage;
    }
    public Page<Tutorial> findByTitle(String keyword, String[] sort, int page, int size) {
        Pageable pageable = createPageable(sort, page, size);

        Page<Tutorial> tutorialPage;
        tutorialPage = tutorialRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        return tutorialPage;
    }

    private Pageable createPageable(String[] sort, int page, int size) {
        String sortField = sort[0];
        String sortDirection = sort[1];

        Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order order = new Sort.Order(direction, sortField);
        return PageRequest.of(page - 1, size, Sort.by(order));
    }

    public void saveTutorial(Tutorial tutorial){
        tutorialRepository.save(tutorial);
    }

    public Optional<Tutorial> findById(Integer id) {
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        return tutorial;
    }

    public void deletedById(Integer id) {
        tutorialRepository.deleteById(id);
    }

    public void updatePublishedStatus(Integer id, boolean status) {
        tutorialRepository.updatePublishedStatus(id, status);
    }
}
