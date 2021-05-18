package com.example.praktiskviewpager;

import java.util.ArrayList;
import java.util.Date;

public class EquipmentList {
    private static ArrayList<Equipment> EquipmentList;

    public EquipmentList() {
        EquipmentList = new ArrayList<>();
        // public Equipment(type, manufacturer, model, loanStatus, loanedTo, imageURL, dateOfPurchase)
        EquipmentList.add(new Equipment("Console", "Sony", "Playstation 4", Equipment.NOT_LOANED, Equipment.NO_LOANER,
                "https://kark.uit.no/~wfa004/d3330/images/playstation1.jpg", new Date()));

        EquipmentList.add(new Equipment("Console", "Nintendo", "Switch", Equipment.LOANED, "Frank Jensen",
                "https://kark.uit.no/~wfa004/d3330/images/playstation1.jpg", new Date()));

        EquipmentList.add(new Equipment("Car", "Toyota", "Supra MKV", Equipment.LOANED, "Fritjof Nansen",
                "https://kark.uit.no/~wfa004/d3330/images/playstation1.jpg", new Date()));

        EquipmentList.add(new Equipment("Car", "BMW", "G30 350D", Equipment.NOT_LOANED, Equipment.NO_LOANER,
                "https://kark.uit.no/~wfa004/d3330/images/playstation1.jpg", new Date()));
    }

    public ArrayList<Equipment> getEquipmentList() {
        return EquipmentList;
    }
}
