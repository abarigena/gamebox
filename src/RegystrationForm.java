import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegystrationForm  extends JDialog{
    private JTextField tfLogin;
    private JTextField tfPassword;
    private JButton registryButton;
    private JButton cancelButton;
    private JPanel RegisterPanel;
    private JPasswordField pfPassword;
    private JPasswordField pfPasswordConf;

    public RegystrationForm(JFrame parent){
        super(parent);
        setTitle("Create a New account");
        setContentPane(RegisterPanel);
        setSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        registryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String login = tfLogin.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confPassword = String.valueOf(pfPasswordConf.getPassword());

        if(login.isEmpty()||password.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Заполните все поля",
                    "Попробуйте снова",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confPassword)){
            JOptionPane.showMessageDialog(this,
                    "Пароли несовпадают",
                    "Попробуйте снова",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        user = addUserToDatabase(login,password);
        if(user != null){
            dispose();
        }else {
            JOptionPane.showMessageDialog(this,
                    "Ошибка регистрации нового пользователя",
                    "Попробуйте снова",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;

    private User addUserToDatabase(String login, String password) {
        final String DB_URL = "jdbc:mysql://localhost:3306/gamebox";
        final String USERNAME = "root";
        final String PASSWORD = "root";
        int startScore = 0;

        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users(login, password, scoreTetris, scoreSnake)"+ "VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3, String.valueOf(startScore));
            preparedStatement.setString(4, String.valueOf(startScore));


            int adedRows = preparedStatement.executeUpdate();
            if(adedRows>0){
                user = new User();
                user.login = login;
                user.password = password;
                user.scoreTetris = String.valueOf(startScore);
                user.scoreSnake = String.valueOf(startScore);

            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }


}
