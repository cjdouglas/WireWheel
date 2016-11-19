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

    /**
     * Returns the title of this listing
     * @return The title of this listing
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this listing
     * @param title The title to name this listing
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the mileage of this listing
     * @return The mileage of this listing
     */
    public String getMileage() {
        return mileage;
    }

    /**
     * Sets the mileage of this listing
     * @param mileage The mileage to set
     */
    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    /**
     * Returns the price of this listing
     * @return The price of this listing
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the price of this listing
     * @param price The price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Returns the text of the listing
     * @return The text of this listing
     */
    public ArrayList<String> getText() {
        return text;
    }

    /**
     * Sets the text of this listing
     * @param text The text to set
     */
    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    /**
     * Returns the link to the key image of this listing
     * @return The link to the key image of this listing
     */
    public String getKeyImageLink() {
        return keyImageLink;
    }

    /**
     * Sets the key image link of this listing
     * @param keyImageLink The link to the key image
     */
    public void setKeyImageLink(String keyImageLink) {
        this.keyImageLink = keyImageLink;
    }

    /**
     * Returns the string representing all links to images separated by a "|"
     * @return a string containing all links to images for that listing separated by "|"
     */
    public String getImageLinks() {
        return imageLinks;
    }

    /**
     * Sets the string representing the image links for this listing
     * @param imageLinks the string containing image links to set for this listing
     */
    public void setImageString(String imageLinks) {
        this.imageLinks = imageLinks;
    }
}
