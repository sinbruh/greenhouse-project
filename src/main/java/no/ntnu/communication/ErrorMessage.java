package no.ntnu.communication;

public class ErrorMessage extends Message {
    public ErrorMessage(String message) {
    }

    public String messageAsString() {
        return "error";
    }
}
