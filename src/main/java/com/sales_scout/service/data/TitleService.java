package com.sales_scout.service.data;

import com.sales_scout.entity.data.Title;
import com.sales_scout.repository.data.TitleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitleService {
    private final TitleRepository titleRepository;

    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    /**
     * Retrieves all titles from the repository.
     *
     * @return a list of all titles.
     */
    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    /**
     * Retrieves a title by its ID.
     *
     * @param id the ID of the title to retrieve.
     * @return an Optional containing the title if found, or empty if not found.
     */
    public Optional<Title> getTitleById(Long id) {
        return this.titleRepository.findById(id);
    }

    /**
     * Adds a new title to the repository.
     *
     * @param title the title to add.
     * @return the added title.
     */
    public Title addTitle(Title title) {
        return this.titleRepository.save(title);
    }

    /**
     * Updates an existing title by its ID.
     *
     * @param id    the ID of the title to update.
     * @param title the title data to update.
     * @return the updated title.
     * @throws EntityNotFoundException if no title with the specified ID exists.
     */
//    public Title updateTitle(Long id, Title personTitle) {
//        return this.titleRepository.findById(id)
//                .map(existingTitle -> {
//                    existingTitle.setTitle( personTitle.getTitle());
//                    return this.titleRepository.save(existingTitle);
//                })
//                .orElseThrow(() -> new EntityNotFoundException("Title with id " + id + " not found."));
//    }

    /**
     * Deletes a title by its ID.
     *
     * @param id the ID of the title to delete.
     * @throws EntityNotFoundException if no title with the specified ID exists.
     * @throws RuntimeException if there is a failure in deleting the title.
     */
    public void deleteTitle(Long id) {
        try {
            this.titleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Title with id " + id + " not found.", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete title with id " + id, e);
        }
    }
}
