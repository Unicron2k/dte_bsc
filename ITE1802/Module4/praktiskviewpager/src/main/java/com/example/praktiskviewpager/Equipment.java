package com.example.praktiskviewpager;

import java.util.Date;

public class Equipment {
    String type, manufacturer, model, loanStatus, loanedTo, imageURL;
    Date dateOfPurchase;

    static final String LOANED = "Loaned", NOT_LOANED = "Not loaned", NO_LOANER = "None";

    public Equipment(String type, String manufacturer, String model, String loanStatus, String loanedTo, String imageURL, Date dateOfPurchase) {
        this.type = type;
        this.manufacturer = manufacturer;
        this.model = model;
        this.loanStatus = loanStatus;
        this.loanedTo = loanedTo;
        this.imageURL = imageURL;
        this.dateOfPurchase = dateOfPurchase;
    }

    public Equipment(String type, String manufacturer, String model, String imageURL, Date dateOfPurchase) {
        this(type, manufacturer, model, NOT_LOANED, NO_LOANER, imageURL, dateOfPurchase);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanedTo() {
        return loanedTo;
    }

    public void setLoanedTo(String loanedTo) {
        this.loanedTo = loanedTo;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }
}
