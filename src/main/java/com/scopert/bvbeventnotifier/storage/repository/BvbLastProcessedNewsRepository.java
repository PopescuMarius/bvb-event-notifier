package com.scopert.bvbeventnotifier.storage.repository;

import com.scopert.bvbeventnotifier.storage.model.BvbLastProcessedNews;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BvbLastProcessedNewsRepository extends CrudRepository<BvbLastProcessedNews, Long> {

    BvbLastProcessedNews findTopByOrderByIdDesc();

}
