package models.enums;

public enum TypeClothes {
    JACKET("куртка"),
    COAT("пальто"),
    SHIRT("рубашка"),
    BLAZER("пиджак"),
    CAP("кепка");

    private final String description;

    private TypeClothes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
