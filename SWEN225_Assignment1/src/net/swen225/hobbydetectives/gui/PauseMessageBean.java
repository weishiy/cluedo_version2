package net.swen225.hobbydetectives.gui;

/**
 * Data for blocking (computer waits) dialogues.
 */
public class PauseMessageBean {
    /**
     * The message to display.
     */
    private String messageText;

    public void messageText(String messageText) {
        this.messageText = messageText;
    }

    public String messageText() {
        return messageText;
    }
}
