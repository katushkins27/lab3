package models.enums;

public enum EmotionalState {
    HAPPY("счастливый"),
    SAD("грустный"),
    CONFUSED("растерянный"),
    CALM("спокойный"),
    AFRAID("испуганный");
    private final String description;

    private EmotionalState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
