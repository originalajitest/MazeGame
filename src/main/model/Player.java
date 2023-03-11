package model;

//Deals with any kind of player movement, deals with player sprites
public class Player {

    private int posX;
    private int posY;

    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void moveX(int inp) {
        posX = posX + inp;
    }

    public void moveY(int inp) {
        posY = posY + inp;
    }

    public void setXAndY(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}