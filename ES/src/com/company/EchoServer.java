package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoServer {

    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    void run() {
        System.out.println("Client side connection established...");
        try (ServerSocket server = new ServerSocket(port);
             Socket clientSocket = server.accept()) {
            handle(clientSocket);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void handle(Socket socket) {
        try (InputStream input = socket.getInputStream();
             InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);
             Scanner scn = new Scanner(isr)) {

            while (true) {
                String msg = scn.nextLine().strip();
                System.out.printf("Got: %s%n", msg);
                sendMsgToClient(new StringBuilder(msg).reverse().toString());
                if ("bye".equalsIgnoreCase(msg)) {
                    System.out.printf(" Bye bye!%n");
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgToClient(String reverseMsg) {

        try (Socket fromServer = new Socket("127.0.0.1", 8788);
             OutputStream output = fromServer.getOutputStream();
             PrintWriter pw = new PrintWriter(output)) {

            pw.write(reverseMsg);
            pw.write(System.lineSeparator());
            pw.flush();

        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}


