package no.ntnu.communication;


/**
 * Represents an error message in the communication protocol.
 * This class extends the abstract Message class.
 */
public class ErrorMessage extends Message {

    /**
     * Constructs an ErrorMessage object.
     * @param message The error message string.
     */
    public ErrorMessage(String message) {
        super();
    }

    /**
     * Converts this error message into a string representation.
     * @return The string representation of this error message, which is always "error".
     */
    public String messageAsString() {
        return "error";
    }
}
