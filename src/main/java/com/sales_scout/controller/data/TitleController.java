package com.sales_scout.controller.data;

import com.sales_scout.entity.data.Title;
import com.sales_scout.service.data.TitleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/titles")
public class TitleController {
    private final TitleService titleService;

    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    /**
     * Retrieves all titles.
     *
     * @return a ResponseEntity containing a list of titles and a 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<Title>> getAllTitles() {
        List<Title> titles = titleService.getAllTitles();
        return ResponseEntity.ok(titles);
    }

    /**
     * Retrieves a title by its ID.
     *
     * @param id the ID of the title to retrieve.
     * @return a ResponseEntity containing the title if found, or a 404 Not Found status if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Title> getTitleById(@PathVariable Long id) {
        Optional<Title> title = titleService.getTitleById(id);
        return title.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Adds a new title.
     *
     * @param title the title to add.
     * @return a ResponseEntity containing the added title and a 201 Created status.
     */
    @PostMapping
    public ResponseEntity<Title> addTitle(@RequestBody Title title) {
        Title createdTitle = titleService.addTitle(title);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTitle);
    }

    /**
     * Updates an existing title by its ID.
     *
     * @param id    the ID of the title to update.
     * @param title the title data to update.
     * @return a ResponseEntity containing the updated title and a 200 OK status, or a 404 Not Found status if not found.
     */
//    @PutMapping("/{id}")
//    public ResponseEntity<Title> updateTitle(@PathVariable Long id, @RequestBody Title title) {
//        try {
//            Title updatedTitle = titleService.updateTitle(id, title);
//            return ResponseEntity.ok(updatedTitle);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    /**
     * Deletes a title by its ID.
     *
     * @param id the ID of the title to delete.
     * @return a ResponseEntity with a 204 No Content status if deleted successfully, or a 404 Not Found or 500 Internal Server Error status if an error occurs.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
        try {
            titleService.deleteTitle(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
