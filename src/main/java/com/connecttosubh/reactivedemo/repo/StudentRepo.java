package com.connecttosubh.reactivedemo.repo;

import com.connecttosubh.reactivedemo.domain.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepo {

    Mono<Student> getById(Integer id);
    Flux<Student> findAll();
}
