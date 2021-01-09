package com.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MultiChatUI extends JFrame { // Runnalbe 상속받아야 되는데..
    private JPanel loginPanel;
    protected JButton loginButton; // 컨트롤러에서 핸들링 필요한 객체는 protected
    private JLabel inLabel;
    protected JLabel outLabel;
    protected JTextField idInput;

    private JPanel logoutPanel;
    protected JButton logoutButton;
    private JPanel msgPanel;
    protected JTextField msgInput;
    protected JButton exitButton;

    protected Container tab;
    protected CardLayout cardLayout;

    protected JTextArea msgOut;

    public String id = "";

    public MultiChatUI() {

        super("::멀티챗::");
        setSize(500, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);

        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        idInput = new JTextField(15);
        loginButton = new JButton("로그인");

        inLabel = new JLabel("대화명");
        loginPanel.add(inLabel, BorderLayout.WEST);
        loginPanel.add(idInput, BorderLayout.CENTER);
        loginPanel.add(loginButton, BorderLayout.EAST);

        logoutPanel = new JPanel();
        logoutPanel.setLayout(new BorderLayout());

        outLabel = new JLabel();
        logoutButton = new JButton("로그아웃");

        logoutPanel.add(outLabel, BorderLayout.CENTER);
        logoutPanel.add(logoutButton, BorderLayout.EAST);

        exitButton = new JButton("종료");
        msgInput = new JTextField();
        msgPanel = new JPanel();
        msgPanel.setLayout(new BorderLayout());

        msgPanel.add(msgInput, BorderLayout.CENTER);
        msgPanel.add(exitButton, BorderLayout.EAST);

        tab = new JPanel();
        cardLayout = new CardLayout();
        tab.setLayout(cardLayout);
        tab.add(loginPanel, "login");
        tab.add(logoutPanel, "logout");
        //cardLayout.show(tab, "logout");

        msgOut = new JTextArea(" ", 10, 30);
        msgOut.setEditable(false);

        JScrollPane jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        getContentPane().add(tab, BorderLayout.NORTH);
        getContentPane().add(jsp, BorderLayout.CENTER);
        getContentPane().add(msgPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);

    } // constructor

    public void addButtonActionListener(ActionListener listener) {
        loginButton.addActionListener(listener);
        logoutButton.addActionListener(listener);
        exitButton.addActionListener(listener);
        msgInput.addActionListener(listener);
    } // addButtonActionListener()
}
