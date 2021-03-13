package com.scopert.bvbeventnotifier.model;


import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    //TODO I just want to expose a simple REST endoint to check if it is accessible from AWS (for the beginning)
    /*
         curl http://localhost:8080
         curl http://localhost:8080/user
         curl -i -H "Content-Type:application/json" -d '{"name": "Frodo", "email": "test@email"}' http://localhost:8080/user
         curl http://localhost:8080/user/search/findByName?name=Frodo
     */
    List<User> findByName(@Param("name") String name);

    List<User> findByEmail(@Param("email") String name);

}
