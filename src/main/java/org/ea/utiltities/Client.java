package org.ea.utiltities;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

/**
 * A simple TCP client that sends JSON-encoded transformation commands to a server.
 *
 * @precondition A server must be running and listening on the specified IP and port.
 * @postcondition Sends messages over a socket connection in JSON format.
 */
public class Client {

    String ip;
    int port;
    Socket socket;

    /**
     * Initializes a new client socket with the given IP and port.
     *
     * @param ip   the target server IP address
     * @param port the target server port number
     * @throws IOException if the connection cannot be established
     * @precondition IP and port must correspond to a reachable server
     * @postcondition A socket is opened and ready for communication
     */
    public Client(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
    }

    /**
     * Entry point for launching the client interactively via terminal.
     *
     * @param args command-line arguments (not used)
     * @throws IOException if network communication fails
     * @precondition Server must be available on localhost:9000
     * @postcondition User can enter messages and send them to the server until "exit"
     */
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

    /**
     * Sends a JSON-encoded transformation message to the connected server.
     * The message format is assumed to be "operation axis value" (e.g., "rotate X 15.0").
     *
     * @param message user input string in space-separated format
     * @throws IOException if writing to the socket fails
     * @precondition Input must be in format: <operation> <axis> <value>
     * @postcondition A valid JSON message is sent to the server
     */
    private void sendMessage(String message) throws IOException {
        TransformMessage transformMessage = new TransformMessage(
                message.split(" ")[0],
                message.split(" ")[1],
                Double.parseDouble(message.split(" ")[2])
        );
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(transformMessage);
        PrintWriter pr = new PrintWriter(this.socket.getOutputStream());
        pr.println(jsonMessage);
        pr.flush();
    }
}
