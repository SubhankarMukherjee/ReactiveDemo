package com.connecttosubh.reactivedemo.repo;

import com.connecttosubh.reactivedemo.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StudentRepoImplTest {
    @Autowired
    StudentRepo repo;

    @BeforeEach
    void setUp() {
        // repo= new StudentRepoImpl();

    }

    @Test
     void testMonoBlock()
    {
        Mono<Student> studentMono = repo.getById(1);
        Student student = studentMono.block();
        System.out.println("Studnet is :"+ student);

    }
    @Test
    void testGetByIdMonoSubscribe()
    {
        Mono<Student> studentMono = repo.getById(1);
        studentMono.subscribe(System.out::println);
    }

    @Test  // no back push
    void testMonoMap()
    {
        Mono<Student> studentMono = repo.getById(1);
        studentMono.map(student -> {
            System.out.println(student.getName());
           return student.getName()+"Title";
       }).subscribe(System.out::println);
    }

    @Test
    void fluxTestBlockFirst()
    {
        Flux<Student> studentFlux = repo.findAll();
        Student student = studentFlux.blockFirst();
        System.out.println(student);
    }
    @Test
    void fluxSubscribe()
    {
        Flux<Student> studentFlux = repo.findAll();
        studentFlux.subscribe(student -> {
            System.out.println(student);
        });
    }

    @Test
    void fluxToListMono()
    {
        Flux<Student> studentFlux = repo.findAll();
        //verify 3 times flux worked
        //this is part of project ractor for testing scope
        StepVerifier.create(studentFlux).expectNextCount(3).verifyComplete();
        Mono<List<Student>> listMono = studentFlux.collectList();
        listMono.subscribe(students -> {
            students.forEach(student -> {
                System.out.println(student);
            });
        });
    }
    @Test
    void fluxFilterMono()
    {
        Flux<Student> studentFlux = repo.findAll();
        Mono<Student> studentMono= studentFlux.filter(s->s.getId()==3).next();
        studentMono.subscribe(student -> {
            System.out.println(student);
        });
    }

    @Test
    void fluxFilterMonoExceptionHandling()
    {
         final Integer id=8;
        Flux<Student> studentFlux = repo.findAll();
        Mono<Student> studentMono= studentFlux.filter(s->s.getId()==id).single(); // single return one element, no element exception, array inex out of bound exception for multiple value return
         studentMono.doOnError(s->{
             System.out.println(s.getMessage().toUpperCase());
         }).onErrorReturn(
              Student.builder().id(0).build()
         ).subscribe(student -> {
            System.out.println(student);
        });
    }

    @Test
    void getByIdTest()
    {
        Mono<Student> studentMono = repo.getById(2);
        //verify mono run or not
        StepVerifier.create(studentMono).expectNextCount(1).verifyComplete();
       studentMono.subscribe(s->{
           System.out.println(s.getName());
        });
    }
}