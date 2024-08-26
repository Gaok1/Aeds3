package Tools;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Logs {

    private static LogsUI logsUI = new LogsUI();

    public static void Alert(String Message) {
        logsUI.appendLog("[ALERT]: " + Message, Color.RED);
    }

    public static void Succeed(String Message) {
        //logsUI.appendLog("[SUCCESS]: " + Message, new Color(0, 200, 0)); // Lighter and brighter green
    }
    

    public static void Details(String Message) {
        //logsUI.appendLog("[DETAILS]: " + Message, Color.CYAN);
    }

    public static void DetailsNoLn(String Message) {
        // color blue
        String BLUE = "\u001B[34m";
        System.out.print(BLUE + Message);
    }

    public static void KindaAlert(String Message) {
        logsUI.appendLog("[KINDA ALERT]: " + Message, new Color(255, 140, 0)); // Dark Orange
    }

    public static void Clear() {
        logsUI.clearLog();
    }

    public static class LogsUI {
        private JFrame frame;
        private JTextPane logTextPane;

        public LogsUI() {
            frame = new JFrame("Logs");
            frame.setSize(800, 600); // Increased size for better readability
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            logTextPane = new JTextPane();
            logTextPane.setEditable(false);
            logTextPane.setFont(new Font("Consolas", Font.PLAIN, 16)); // Use Consolas font, size 16
            logTextPane.setBackground(new Color(30, 30, 30)); // Dark background color
            logTextPane.setForeground(Color.WHITE); // White text color
            JScrollPane scrollPane = new JScrollPane(logTextPane);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            frame.setVisible(true);
        }

        public void appendLog(String log, Color color) {
            StyledDocument doc = logTextPane.getStyledDocument();
            Style style = logTextPane.addStyle("Style", null);
            StyleConstants.setForeground(style, color);
            StyleConstants.setFontSize(style, 16); // Increase font size

            try {
                doc.insertString(doc.getLength(), log + "\n", style);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void clearLog() {
            logTextPane.setText("");
        }
    }
}
