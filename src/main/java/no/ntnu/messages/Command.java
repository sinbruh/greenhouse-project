package no.ntnu.messages;

public abstract class Command extends Message{

    public abstract Message execute();
}
