package no.ntnu.communication;

import no.ntnu.greenhouse.GreenhouseSimulator;

public abstract class Command extends Message{

    public abstract Message execute(GreenhouseSimulator simulator);
}
