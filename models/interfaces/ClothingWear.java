package models.interfaces;

import models.clothes.Clothing;
import models.exceptions.ClothingException;

public interface ClothingWear {
    void wearClothes(Clothing clothes);
    Clothing getClothes();
    void setClothes(Clothing clothes) throws ClothingException;
}
