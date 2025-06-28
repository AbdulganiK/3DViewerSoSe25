package org.ea.utiltities;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    String ip;
    int port;
    Socket socket;
    public Client(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
    }

    public static void main(String[] args) throws IOException {
        Client c = new Client("localhost", 9000);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (Objects.equals(input, "exit")) {
                break;
            }
            c.sendMessage(input);
        }
    }

    private void sendMessage(String message) throws IOException {
        TransformMessage transformMessage = new TransformMessage(message.split(" ")[0], message.split(" ")[1], Double.parseDouble(message.split(" ")[2]));
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(transformMessage);
        PrintWriter pr = new PrintWriter(this.socket.getOutputStream());
        pr.println(jsonMessage);
        pr.flush();
    }
}
