package org.example;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String nickname;

    public Client() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(new File("settings.txt")));
        int SERVER_PORT = Integer.parseInt(props.getProperty("SERVER_PORT"));
        try {
            String address = "localhost";
            this.socket = new Socket(address, SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Ошибка при создании сокета!");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            nickname = this.pressNickname();
            new ReadChat().start();
            new WriteChat().start();
        } catch (IOException e) {
            downSocket();
        }
    }

    public static void main(String[] args) throws IOException {
        new Client();
    }

    public String pressNickname() {
        System.out.print("Введите ваше имя: ");
        try {
            nickname = inputUser.readLine();
            out.write("Привет, " + nickname + "!\n");
            out.flush();
        } catch (IOException ignored) {
        }
        return nickname;
    }

    private void downSocket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
        socket.isClosed();
    }

    private class ReadChat extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = in.readLine();
                    if (str.equals("/exit")) {
                        downSocket();
                        break;
                    }
                    System.out.println(str);
                }
            } catch (IOException e) {
                downSocket();
            }
        }
    }

    public class WriteChat extends Thread {
        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    userWord = inputUser.readLine();
                    if (userWord.equals("/exit")) {
                        out.write("/exit" + "\n");
                        downSocket();
                        break;
                    } else {
                        out.write(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").format(Calendar.getInstance().getTime())
                                + nickname + ": " + userWord + "\n");
                    }
                    out.flush();
                } catch (IOException e) {
                    downSocket();
                }
            }
        }
    }
}