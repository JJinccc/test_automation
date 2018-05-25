package com.ffan.qa.common.model.runner;

import com.ffan.qa.common.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Task {
    private String name;
    private String file;
    private String type;
    private String inform;
    private List<String> informPhoneList;
    private List<String> informEmailList;

    public Task(String name, String file, String type, String inform) {
        this.name = name;
        this.file = file;
        this.type = type;
        this.inform = inform;
        informEmailList = new ArrayList<>();
        informPhoneList = new ArrayList<>();
    }

    public void generateInfoList(Map<String, Person> personMap) {
        String[] persons = inform.split(",");
        for (String p:
             persons) {
            informPhoneList.add(personMap.get(p).getPhone());
            informEmailList.add(personMap.get(p).getEmail());
        }
    }
}
