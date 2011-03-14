package newtest;

/*
 * BadBoardException.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log$
 *
 */


/**
 * An exception class to handle bad creation of the board.
 *
 * @author Nick Poorman 
 */
public class BadBoardException extends Exception {

    /**
     * No argmument constructor for this object.
     */
    public BadBoardException() {
        super();
    }

    /**
     * Constructor for this object.
     * 
     * @param msg String message
     */
    public BadBoardException(String msg) {
        super(msg);
    }
}
