package com.epam.trainingservice.entity.enums;

public enum ActionType {
    ADD("ADD"),
    CANCEL("CANCEL");

    private final String value;

    private ActionType(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
