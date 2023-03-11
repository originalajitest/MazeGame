package model;

public class InvalidInputException extends Exception {
    public InvalidInputException(String statement) {
        super(statement);
    }
}