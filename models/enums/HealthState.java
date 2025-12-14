package models.enums;

public enum HealthState {
    SERIOUSLI_ILL("серьезно болен", 8),
    SICK("болен", 4),
    MILDLY_ILL("легко болен", 2),
    NOT_FEELING_WELL("недомогает", 1),
    ALMOST_HEALTHY("почти здоров", 0),
    HEALTHY("здоров", 0);

    private final String description;
    final private int extraDays;

    private HealthState(String description, int extraDays) {
        this.description = description;
        this.extraDays = extraDays;
    }

    public String getDescription() {
        return description;
    }
    public int getExtraDays(){
        return extraDays;
    }
}
