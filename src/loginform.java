import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginform extends JDialog {
    private JTextField tfLogin;
    private JButton buttonCancel;
    private JButton buttonOk;
    private JPasswordField pfPassword;
    private JPanel loginPanel;

    public loginform(JFrame parent){
        super(parent);
        setTitle("login");
        setContentPane(loginPanel);
        setSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = tfLogin.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticatedUser(login,password);

                if(user!=null){
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(loginform.this,
                            "Логин или пароль неправильный","Попробуйте снова",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    public User user;
    private User getAuthenticatedUser(String login, String password) {

        final String DB_URL = "jdbc:mysql://localhost:3306/gamebox";
        final String USERNAME = "root";
        final String PASSWORD = "root";

        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE login=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = new User();
                user.login = resultSet.getString("login");
                user.password = resultSet.getString("password");
                user.scoreTetris = resultSet.getString("scoreTetris");
            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

}
