package com.scopert.bvbeventnotifier.storage.services;

import com.scopert.bvbeventnotifier.storage.model.BvbLastProcessedNews;
import com.scopert.bvbeventnotifier.storage.repository.BvbLastProcessedNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class BvbLastProcessedNewsService {

    @Autowired
    public BvbLastProcessedNewsRepository repository;

    public BvbLastProcessedNews saveLastProcesedNews(String title){
        return repository.save(new BvbLastProcessedNews(title, LocalDateTime.now(ZoneId.of("Europe/Bucharest"))));
    }

    public BvbLastProcessedNews findTopByOrderByIdDesc(){
        return repository.findTopByOrderByIdDesc();
    }

}
