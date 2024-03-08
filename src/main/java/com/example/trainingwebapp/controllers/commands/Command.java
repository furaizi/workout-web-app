package com.example.trainingwebapp.controllers.commands;

import java.util.HashMap;
import java.util.NoSuchElementException;

public enum Command {
    LIST("LIST"),
    ADD("ADD"),
    LOAD("LOAD"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    LOAD_TO_UPDATE("LOAD_TO_UPDATE");

    Command(String command) {
        this.command = command;
    }

    private String command;

    public static Command get(String str) {
        HashMap<String, Command> commands = new HashMap<>() {{
           put(LIST.command, LIST);
            put(ADD.command, ADD);
            put(LOAD.command, LOAD);
            put(UPDATE.command, UPDATE);
            put(DELETE.command, DELETE);
            put(LOAD_TO_UPDATE.command, LOAD_TO_UPDATE);
        }};

        if (str == null || commands.get(str) == null)
            return LIST;
        return commands.get(str);
    }
}
