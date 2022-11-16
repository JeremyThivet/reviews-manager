package org.jeremyworkspace.reviewsmanager.api.repository;

import org.jeremyworkspace.reviewsmanager.api.model.Entry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends CrudRepository<Entry, Long> {

}
