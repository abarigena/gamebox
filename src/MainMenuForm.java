import hockey.HockeyGame;
import hockey.server.GameServer;
import tetris.main.GamePanel;
import tetris.main.PlayManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;


public class MainMenuForm extends JDialog {
    private JPanel MainMenuPanel;
    private JPanel TetrisGamePanel;
    private JPanel SnakeGamePanel;
    private JButton buttonStartGame;
    private JButton updateButton;
    private JLabel scoreUserMax;
    private JTable scoreTableMaxUsers;
    private JButton CreateHubButton;
    private JButton ConnectHubButton;
    private GamePanel gamePanel;

    private ArrayList<String[]> dataArrayList;

    public MainMenuForm(JFrame parent){
        super(parent);
        setTitle("login");
        setContentPane(MainMenuPanel);
        setSize(new Dimension(1280, 720));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        updateMaxScoreUser();
        addData();


        buttonStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTetris();


            }
        });
        setVisible(true);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( PlayManager.score > Integer.parseInt(User.scoreTetris)){
                    updateScoreTetrisDataBase(PlayManager.score);
                }
            }
        });
        CreateHubButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String portStr = JOptionPane.showInputDialog("Enter the port number:");
                int port = Integer.parseInt(portStr);
                new Thread(() -> {
                    GameServer gs = new GameServer(port);
                    gs.acceptConnections();
                }).start();
            }
        });
        ConnectHubButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HockeyGame game = new HockeyGame();
            }
        });
    }

    private void updateMaxScoreUser() {
        scoreUserMax.setText(User.scoreTetris);
    }

    private void startTetris() {
        JFrame window = new JFrame("tetris");
        window.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                gamePanel.stopGame(); // Остановить игру при закрытии окна
            }
        });
        PlayManager playManager = new PlayManager();
        gamePanel.setPlayManager(playManager);

        gamePanel.launchGame();

    }
    public User user;

    private void addData(){
        final String DB_URL = "jdbc:mysql://localhost:3306/gamebox";
        final String USERNAME = "root";
        final String PASSWORD = "root";


        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = conn.createStatement();

            String updateScoreSQL = "SELECT login, scoreTetris FROM users ORDER BY scoreTetris DESC LIMIT 5";
            PreparedStatement preparedStatement = conn.prepareStatement(updateScoreSQL);

            ResultSet resultSet = preparedStatement.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"login", "scoreTetris"}, 0);


            while(resultSet.next()){
                String login = resultSet.getString("login");
                String scoretetris = resultSet.getString("scoreTetris");
                model.addRow(new Object[]{login, scoretetris});

            }
            scoreTableMaxUsers.setModel(model);

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private User updateScoreTetrisDataBase(int maxScore) {
        final String DB_URL = "jdbc:mysql://localhost:3306/gamebox";
        final String USERNAME = "root";
        final String PASSWORD = "root";


        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);


            String updateScoreSQL = "UPDATE users SET scoreTetris = ? WHERE login = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateScoreSQL);
            preparedStatement.setInt(1, maxScore); // Устанавливаем новый максимальный счет
            preparedStatement.setString(2, User.login);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Очки Tetris успешно обновлены.");
            } else {
                System.out.println("Ошибка обновления очков Tetris.");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;

    }

}
