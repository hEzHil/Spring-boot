package com.opensource;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class ,args);
    }




    // first API in springboot and within the main class and its demo only
//    @GetMapping("/greet")
//    public GreetResponse greet(){
//        return new GreetResponse(
//                "hello",
//                List.of("Java","C++","GoLang"),
//                new Person("Ezhil",22,30_000)
//        );
//    }

    // record class are immutable data
    // these store toString,getter and setter method, hashcode

//    record Person(String name, int age, double salary){}
//    record GreetResponse(
//            String greet,
//            List<String> favProgrammingLanguage,
//            Person person
//    ){}

    // this exact GreetResponse method will do same thing in record class
    // so much of lines are there
    // so we are using record class
//    class GreetResponse {
//        private String greet;
//
//        public GreetResponse(String greet) {
//            this.greet = greet;
//        }
//
//        public String getGreet() {
//            return greet;
//        }
//
//        @Override
//        public String toString() {
//            return "GreetResponse{" +
//                    "greet='" + greet + '\'' +
//                    '}';
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            GreetResponse that = (GreetResponse) o;
//            return Objects.equals(greet, that.greet);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(greet);
//        }
//    }
}
