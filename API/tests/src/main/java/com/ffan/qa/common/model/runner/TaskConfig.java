package com.ffan.qa.common.model.runner;

import com.ffan.qa.common.model.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class TaskConfig {
    private ArrayList<Person> persons;
    private String root;
    private String classpath;
    private String mode;
    private Integer interval;
    private boolean alarm;
    private ArrayList<Task> tasks;
    private Map<String, Person> personMap;

    public TaskConfig() {
        persons = new ArrayList<Person>();
        personMap = new HashMap<>();
        tasks = new ArrayList<Task>();
    }

    public void addPerson(Person person) {
        persons.add(person);
        personMap.put(person.getWx(), person);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}
