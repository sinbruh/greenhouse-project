package no.ntnu.communication;

public class ErrorMessage extends Message {
    public ErrorMessage(String message) {
        super();
    }

    public String messageAsString() {
        return "error";
    }
}
