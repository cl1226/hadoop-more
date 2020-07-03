package com.cnnc.domain;

import java.util.Objects;

public class Student {

    private Long id;

    private String name;

    private Long age;

    public Student(Long id, String name, Long age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return name.equals(student.name) &&
                age.equals(student.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    public static void main(String[] args) {
        Student s1 = new Student(1L, "a", 10L);
        Student s2 = new Student(2L, "b", 20L);
        Student s3 = new Student(2L, "b", 20L);

        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());
        System.out.println(s3.hashCode());
    }

}
