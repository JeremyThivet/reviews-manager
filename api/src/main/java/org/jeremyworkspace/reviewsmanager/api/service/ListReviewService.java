package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.ListReview;

import java.util.Optional;

public interface ListReviewService {
    ListReview saveList(ListReview listReview);

    Iterable<ListReview> findListsByUserId(Long id);

    void deleteListById(Long id);

    Optional<ListReview> getListById(Long id);
}
