package com.asif.pagination.and.filtering.tutorial.controllers;

import com.asif.pagination.and.filtering.tutorial.database.entities.Tutorial;
import com.asif.pagination.and.filtering.tutorial.services.TutorialService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/tutorials")
public class TutorialController {
    private final TutorialService tutorialService;

    @GetMapping("")
    public String getAll(Model model,
                         @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "6") int size,
                         @RequestParam(defaultValue = "id,asc") String[] sort) {
        try {
            List<Tutorial> tutorialList = new ArrayList<Tutorial>();

            Page<Tutorial> tutorialPage;

            if (keyword == null) {
                tutorialPage = tutorialService.findAll(sort, page, size);
            } else {
                tutorialPage = tutorialService.findByTitle(keyword, sort, page, size);
                model.addAttribute("keyword", keyword);
            }
            tutorialList = tutorialPage.getContent();

            model.addAttribute("tutorials", tutorialList);
            model.addAttribute("currentPage", tutorialPage.getNumber() + 1);
            model.addAttribute("totalItems", tutorialPage.getTotalElements());
            model.addAttribute("totalPages", tutorialPage.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortField", sort[0]);
            model.addAttribute("sortDirection", sort[1]);
            model.addAttribute("reverseSortDirection", sort[1].equals("asc") ? "desc" : "asc");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "tutorials";
    }

    @GetMapping("/new")
    public String addTutorial(Model model) {
        Tutorial tutorial = new Tutorial();
        tutorial.setPublished(true);

        model.addAttribute("tutorial", tutorial);
        model.addAttribute("pageTitle", "Create new Tutorial");

        return "tutorial_form";
    }

    @PostMapping("/save")
    public String saveTutorial(Tutorial tutorial, RedirectAttributes redirectAttributes) {
        try {
            tutorialService.saveTutorial(tutorial);
            redirectAttributes.addFlashAttribute("message", "The Tutorial has been saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        }
        return "redirect:/tutorials";
    }

    @GetMapping("/edit/{id}")
    public String editTutorial(@PathVariable("id") Integer id,
                               Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Tutorial> tutorial = tutorialService.findById(id);
            model.addAttribute("tutorial", tutorial.get());
            model.addAttribute("pageTitle", "Edit Tutorial (ID: " + id + ")");
            return "tutorial_form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/tutorials";
    }

    @PostMapping("/delete/{id}")
    public String deleteTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            tutorialService.deletedById(id);

            redirectAttributes.addFlashAttribute("message", "The Tutorial with id=" + id + " has been deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/tutorials";
    }
}
