package net.swen225.hobbydetectives.actions.model;

public interface Action {
    boolean accept(String userInput);

    String description();
    void perform();
}
