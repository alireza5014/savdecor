package ir.savdecor.omid.savdecor.Models;

import org.json.JSONArray;



public class HomeList {

    private int id;
    private int samples_count;
    private String title;
    private String image_path;
    private JSONArray product;

    public int getSamples_count() {
        return samples_count;
    }

    public void setSamples_count(int samples_count) {
        this.samples_count = samples_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public JSONArray getProduct() {
        return product;
    }

    public void setProduct(JSONArray product) {
        this.product = product;
    }
}
