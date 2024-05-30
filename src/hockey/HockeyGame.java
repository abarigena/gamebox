package hockey;

import javax.swing.*;
import java.awt.*;
public class HockeyGame extends JFrame {
    GamePanel gamePanel;
    public HockeyGame(){
        String portStr = JOptionPane.showInputDialog("Enter the server port:");
        int port = Integer.parseInt(portStr);

        gamePanel = new GamePanel(port);
        gamePanel.createPlayers();

        setSize(817, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        add(gamePanel);


        setVisible(true);
        setTitle("Hockey Game "+ gamePanel.playerID);

    }
}
