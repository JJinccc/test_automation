package com.ffan.qa.common.model.runner;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DiffResult {
    private String city;
    private String plazaId;
    private String plazaName;
    private List<DiffResultItem> diffItems;

    public DiffResult() {
        diffItems = new ArrayList<>();
    }

    public void addDiffItem(DiffResultItem item) {
        diffItems.add(item);
    }
}
