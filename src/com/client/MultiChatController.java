package com.client;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiChatController extends Thread {

    private final MultiChatUI v;
    private final MultiChatData chatData;
    private Logger logger;

    // Message 객체를 json 객체로 파싱하기 위한 Gson 객체 생성
    private Gson gson = new Gson();

    private Socket s;

    // 입출력 스트림
    private BufferedReader inMsg = null; // 따로 설정 안해도 되나?
    private PrintWriter outMsg = null;

    private boolean status;

    private Message m;

    private Thread thread;

    public MultiChatController(MultiChatData chatData, MultiChatUI v) {
        // 로거 객체 초기화
        logger = Logger.getLogger(this.getClass().getName());

        this.v = v;
        this.chatData = chatData;
    }

    public void appMain() {

        chatData.addObj(v.msgOut);

        v.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = e.getSource();

                if(obj == v.exitButton) {
                    System.exit(0);
                }
                else if(obj == v.loginButton) {
                    v.id = v.idInput.getText();
                    v.outLabel.setText(" 대화명 : " + v.id);
                    v.cardLayout.show(v.tab, "logout");
                    connectServer();
                }
                else if(obj == v.logoutButton) {
                    outMsg.println(gson.toJson(new Message(v.id, "", "", "logout")));
                    v.msgOut.setText("");
                    v.cardLayout.show(v.tab, "login");
                    outMsg.close();
                    try {
                        inMsg.close();
                        s.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    status = false;
                }
                else if (obj == v.msgInput) {
                    String input = v.msgInput.getText();
                    String data = null;
                    if(input.startsWith("[secret]")) { // 실행 시 [secret] + "/" + 상대방 이름 + "/" + msg 형태로...
                        String strings[] = input.split("/");
                        if(strings.length>=3) {
                            data = gson.toJson(new Message(v.id, "", strings[2], "secret", strings[1]));
                            v.msgInput.setText("");
                        }
                        else {
                            v.msgInput.setText("잘못된 형식입니다.");
                        }
                    }
                    else {
                        data = gson.toJson(new Message(v.id, "", v.msgInput.getText(), "msg"));
                        v.msgInput.setText("");
                    }
                    outMsg.println(data);
                }
            }
        });
    }

    public void connectServer() {
        try {
            s = new Socket("127.0.0.1", 7777);
            logger.log(Level.INFO, "[Client]Server 연결 성공!!");
            inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outMsg = new PrintWriter(s.getOutputStream(), true);

            m = new Message(v.id, "", "", "login");
            outMsg.println(gson.toJson(m));

            thread = new Thread(this);
            thread.start();

        } catch (Exception e) {
            logger.log(Level.WARNING, "[MultiChatUI]connectServer() Exception 발생!!");
            e.printStackTrace();
        }
    }

    public void run() {
        String msg;
        status = true;

        while (status) {
            try {
                msg = inMsg.readLine();
                m = gson.fromJson(msg, Message.class);

                // MultiChatDat 객체로 데이터 갱신
                chatData.refreshData(m.getId() + ">" + m.getMsg() + "\n");

                // 커서를 현재 대화 메세지에 표시
                v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
            } catch (IOException e) {
                logger.log(Level.WARNING, "[MultiChatUI]메세지 스트림 종료!!");
            }
        }
        logger.info("[MultiChatUI]" + thread.getName() + " 메세지 수신 스레드 종료됨!!");
    }

    public static void main(String[] args) {
        MultiChatController app = new MultiChatController(new MultiChatData(), new MultiChatUI());
        app.appMain();
    }


}
