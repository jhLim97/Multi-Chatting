package com.client;

import javax.swing.*;

public class MultiChatData {

    private JTextArea msgOut;

    public void addObj(JComponent comp) { msgOut = (JTextArea) comp;}

    public void refreshData(String msg) { msgOut.append(msg); }

}
