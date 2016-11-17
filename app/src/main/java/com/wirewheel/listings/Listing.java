package com.wirewheel.listings;

import java.util.ArrayList;

/**
 * Created by Chris on 9/15/2016.
 *
 * This class holds the data associated with a given listing
 */
public class Listing {

    private String link;
    private String title;
    private String price;
    private String mileage;
    private ArrayList<String> text;
    private String keyImageLink;
    private String imageLinks;

    /**
     * No-args Constructor for a Listing
     *
     * Initializes values to their default
     */
    public Listing() {
        link = "";
        title = "";
        price = "Unlisted Price";
        mileage = "Unlisted Mileage";
        text = new ArrayList<>();
        keyImageLink = "";
        imageLinks = "";
    }

    /**
     * Returns the link associated with this listing
     * @return The link associated with this listing
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link of this listing
     * @param link The link to associate with this listing
     */
    public void setLink(String link) {
        this.link = link;
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

    public ArrayList<String> getText() {
        return text;
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    public String getKeyImageLink() {
        return keyImageLink;
    }

    public void setKeyImageLink(String keyImageLink) {
        this.keyImageLink = keyImageLink;
    }

    public String getImageLinks() {
        return imageLinks;
    }

    public void setImageString(String imageLinks) {
        this.imageLinks = imageLinks;
    }

    /*
    public void addImage(String link) {
        imageLinks.add(link);
    }
    */

    public void addTextLine(String line) {
        text.add(line);
    }
}
