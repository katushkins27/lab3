package models.enums;

public enum Size {
    SMALL("маленького"),
    NORMAL("среднего"),
    HUGE("огромного");
    private final String description;

    private Size(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}