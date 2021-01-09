package server;

import com.client.Message;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MultiChatServer {

    private ServerSocket ss = null;
    private Socket s = null; // client를 받기 위한 매개체
    private boolean status = true;

    // 연결된 client 스레드를 관리하는 ArrayList
    ArrayList<ChatThread> chatThreadList = new ArrayList<ChatThread>();

    Logger logger; // 로거 객체 선언

    private void start() {
        logger = Logger.getLogger(this.getClass().getName());

        try {
            ss = new ServerSocket(7777); // 임의의 포트번호를 통해 서버 소켓 생성
            logger.info("MultiChatServer start");

            while(true) {
                s = ss.accept();

                ChatThread chat = new ChatThread();
                chatThreadList.add(chat);
                chat.start();
            }

        } catch (Exception e) {
            logger.info("[MultiChatServer]start() Exception 발생!!");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        MultiChatServer multiChatServer = new MultiChatServer();
        multiChatServer.start();
    }

    class ChatThread extends Thread {

        String msg;

        Message m = new Message();

        Gson gson = new Gson(); // Message 객체를 json 객체로 파싱하기 위한 Gson 객체 생성

        // 입출력 스트림
        private BufferedReader inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
        private PrintWriter outMsg = new PrintWriter(s.getOutputStream(), true);

        public String id = null;

        public ChatThread() throws IOException {}

        public void run() {
            while(status) { // 상태 정보가 true 일경우 반복문을 돌며 수신된 메세지 처리
                try{
                    msg = inMsg.readLine(); // 메세지 수신
                    m = gson.fromJson(msg, Message.class); // Message 클래스로 매핑


                    if(m.getType().equals("logout")) {
                        chatThreadList.remove(this);
                        msgSendAll(gson.toJson(new Message(m.getId(), "", "님이 종료했습니다.", "server")));

                        status = false; // 로그아웃 한 클라이언트 상태 false로 변경
                    }
                    else if (m.getType().equals("login")) {
                        if(id == null) id = m.getId(); // 여기서 id 입력 해 둬야 나중에 secret 같은 기능 구현 가능
                        msgSendAll(gson.toJson(new Message(m.getId(), "", "님이 로그인했습니다.", "server")));
                    }
                    else if(m.getType().equals("secret")) {
                        for (ChatThread ct : chatThreadList) {
                            if (ct.id.equals(m.getTarget())) {
                                ct.outMsg.println(gson.toJson(new Message(m.getId(), "", m.getMsg(), "secret")));
                            }
                        }
                    }
                    else {
                        msgSendAll(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    status = false;
                }

            }
            this.interrupt();
            logger.info(this.getName() + " 종료됨!!");
        }
        public void msgSendAll(String msg) {
            for(ChatThread ct : chatThreadList) {
                ct.outMsg.println(msg);
            }
        }
    }

}
