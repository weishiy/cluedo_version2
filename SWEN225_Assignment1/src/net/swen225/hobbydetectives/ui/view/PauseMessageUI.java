package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;

import java.util.concurrent.Future;

public interface PauseMessageUI {
    /**
     * Creates a dialogue that waits to be dismissed, returning a future that indicates dismissal.
     *
     * @param pauseMessageBean The data used to make the dialogue.
     * @return The future that indicates the dialogue has been dismissed. Contains <code>null</code>.
     */
    Future<Void> render(PauseMessageBean pauseMessageBean);
}
