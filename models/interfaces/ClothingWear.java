package models.interfaces;

import models.clothes.Clothing;

public interface ClothingWear {
    void wearClothes(Clothing clothes);
    Clothing getClothes();
    void setClothes(Clothing clothes);
}
