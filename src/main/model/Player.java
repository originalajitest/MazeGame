package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.HashMap;

//Deals with any kind of player movement, deals with player sprites, deals with player location
public class Player implements Writable {

    private int posX;
    private int posY;

    //EFFECTS: converts Player to a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("posX", posX);
        json.put("posY", posY);
        return json;
    }

    //REQUIRES: posX>=0 and posY>=0
    //MODIFIES: this
    //EFFECTS: initializes location to given inputs.
    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    //REQUIRES: json is an object
    //MODIFIES: this
    //EFFECTS: gets x and y from json and sets posX and posY to gotten values.
    public Player(Object json) {
        this.posX = (int) ((HashMap) json).get("posX");
        this.posY = (int) ((HashMap) json).get("posY");
    }

    //EFFECTS: returns current posX
    public int getX() {
        return posX;
    }

    //EFFECTS: returns current posY
    public int getY() {
        return posY;
    }

    //REQUIRES: inp is an integer.
    //MODIFIES: this.posX
    //EFFECTS: increases or decreases posX by given amount, allows player for horizontal movement.
    public void moveX(int inp) {
        posX = posX + inp;
    }

    //REQUIRES: inp is an integer.
    //MODIFIES: this.posY
    //EFFECTS: increases or decreases posY by given amount, allows player for vertical movement.
    public void moveY(int inp) {
        posY = posY + inp;
    }

    //REQUIRES: posX and posY are integers >=0
    //MODIFIES: this
    //EFFECTS: changes posX and posY to the points give, usually to end of maze for testing function effectiveness,
    // should not be used outside debugging.
    public void setXAndY(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}