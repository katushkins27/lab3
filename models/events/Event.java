package models.events;

public record Event(String description, String location) {
    @Override
    public String toString() {
        return String.format("%s в %s", description, location);
    }
}