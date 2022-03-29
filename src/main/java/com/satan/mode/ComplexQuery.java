package com.satan.mode;

import lombok.Data;

import java.util.List;

@Data
public class ComplexQuery {
    private String type;
    private List<Integer> ids;
    private List<String> emails;
}
