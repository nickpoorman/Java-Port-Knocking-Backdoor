package teadatabasegui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ToDoListPanel extends JPanel {

    public ToDoListPanel() {
        this.setLayout(new BorderLayout());
        Calendar calendar = new Calendar();
        this.add(calendar, BorderLayout.CENTER);
        calendar.setVisible(true);
    }
}
