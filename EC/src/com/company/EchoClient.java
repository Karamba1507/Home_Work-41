package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    static EchoClient connectTo(int port) {
        String localhost = "127.0.0.1";
        return new EchoClient(port, localhost);
    }

    void run() {
        try (Socket socket = new Socket(host, port);
             Scanner scn = new Scanner(System.in, StandardCharsets.UTF_8);
             OutputStream output = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(output)) {

            System.out.printf("Write 'bye' to close... %n");

            while (true) {
                String msg = scn.nextLine();
                pw.write(msg);
                pw.write(System.lineSeparator());
                pw.flush();
                getMsgFromServer();
                if ("bye".equalsIgnoreCase(msg)) {
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMsgFromServer() {

        try (ServerSocket socket = new ServerSocket(8788);
             Socket fromClient = socket.accept();
             InputStream input = fromClient.getInputStream();
             InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
             Scanner scn = new Scanner(isr)) {
            String msg = scn.nextLine().strip();
            System.out.println(msg);

        } catch (IOException e) {
            e.getMessage();
        }
    }
}
