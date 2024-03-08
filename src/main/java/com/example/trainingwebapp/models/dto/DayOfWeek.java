package com.example.trainingwebapp.models.dto;

import java.util.HashMap;

public enum DayOfWeek {
    MONDAY("Monday", "Понеділок", "Понедельник"),
    TUESDAY("Tuesday", "Вівторок", "Вторник"),
    WEDNESDAY("Wednesday", "Середа", "Среда"),
    THURSDAY("Thursday", "Четвер", "Четверг"),
    FRIDAY("Friday", "П'ятниця", "Пятница"),
    SATURDAY("Saturday", "Субота", "Суббота"),
    SUNDAY("Sunday", "Неділя", "Воскресенье");

    private String[] values;

    DayOfWeek(String... values) {
        this.values = values;
    }

    public static DayOfWeek getByString(String str) {
        HashMap<String, DayOfWeek> map = new HashMap<>() {{
            put(MONDAY.toString(), MONDAY);
            put(TUESDAY.toString(), TUESDAY);
            put(WEDNESDAY.toString(), WEDNESDAY);
            put(THURSDAY.toString(), THURSDAY);
            put(FRIDAY.toString(), FRIDAY);
            put(SATURDAY.toString(), SATURDAY);
            put(SUNDAY.toString(), SUNDAY);
        }};

        return map.get(str);
    }


    @Override
    public String toString() {
        return values[0];
    }

    public String toString(String locale) {
        switch (locale.toUpperCase()) {
            case "EN": return values[0];
            case "UK": return values[1];
            case "RU": return values[2];
            default: return values[0];
        }
    }
}
