package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.repository.ListReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ListReviewServiceImpl implements ListReviewService {

    @Autowired
    private ListReviewRepository listReviewRepository;

    @Override
    public ListReview saveList(ListReview listReview){

        // Set creation and last update dates.
        listReview.setCreationDate(new Date());
        listReview.setLastUpdate(new Date());

        ListReview l = this.listReviewRepository.save(listReview);
        return l;
    }

    @Override
    public Iterable<ListReview> findListsByUserId(final Long id){
        return this.listReviewRepository.findByOwnerId(id);
    }

    @Override
    public void deleteListById(final Long id){
        this.listReviewRepository.deleteById(id);
    }

    @Override
    public Optional<ListReview> getListById(final Long id){
        return this.listReviewRepository.findById(id);
    }

}
