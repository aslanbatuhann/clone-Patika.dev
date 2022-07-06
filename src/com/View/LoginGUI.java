package com.View;

import com.Helper.Config;
import com.Helper.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wrappertop;
    private JPanel wrapperbot;
    private JTextField fld_user_uname;
    private JButton btn_login;
    private JPasswordField fld_user_pass;

    public LoginGUI() {
        add(wrapper);
        setSize(600, 600);
        setLocation(Helper.screenCentrePoint("x", getSize()), Helper.screenCentrePoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_uname)||Helper.isFieldEmpty(fld_user_pass)){
                Helper.showMsg("fill");
            }else{

            }

        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI loginGUI = new LoginGUI();
    }
}
