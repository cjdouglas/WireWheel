package com.wirewheel.listings;

/**
 * Created by Chris on 9/15/2016.
 */
public class Listing {

    private String title;
    private String price;
    private String mileage;
    private int photoResource;

    public Listing() {
        title = "";
        price = "";
        mileage = "";
        photoResource = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPhotoResource() {
        return photoResource;
    }

    public void setPhotoResource(int photoResource) {
        this.photoResource = photoResource;
    }
}
