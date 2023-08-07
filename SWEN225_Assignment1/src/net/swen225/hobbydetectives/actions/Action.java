package net.swen225.hobbydetectives.actions;

public interface Action {
    boolean accept(String userInput);

    public String description();
    public void perform();
}
