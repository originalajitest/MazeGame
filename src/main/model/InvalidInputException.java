package model;

//Deals with inputs which are not possible
public class InvalidInputException extends Exception {
    //REQUIRES: statement is a string
    //MODIFIES: statement in Exception class
    //EFFECTS: keeps a readable output of error and keeps program running, enables traceback if e.getMessage()
    // changed to e.printStackTrace()
    public InvalidInputException(String statement) {
        super(statement);
    }
}