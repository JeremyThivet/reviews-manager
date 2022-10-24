package org.jeremyworkspace.reviewsmanager.api.repository;

import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListReviewRepository extends CrudRepository<ListReview, Long> {

    Iterable<ListReview> findByOwnerId(Long id);

}
