package com.connecttosubh.reactivedemo.repo;

import com.connecttosubh.reactivedemo.domain.Student;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentRepoImpl implements StudentRepo {

    Student sam= new Student(1,"Sam",12);
    Student ram=new Student(2,"Ram",15);
    Student ham=new Student(3,"Ham",14);


    @Override
    public Mono<Student> getById(Integer id) {
       // return Mono.just(sam);
        Flux<Student> allFlux = findAll();
        Mono<Student> returnedStudent= allFlux.filter(s->s.getId()==id).next();
        return returnedStudent;


    }

    @Override
    public Flux<Student> findAll() {

        return Flux.just(sam,ram,ham);
    }
}
