package org.ea.utiltities;

import com.google.gson.Gson;
import org.ea.controller.MeshController;
import org.ea.controller.StageController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Server implements Runnable {
    private int port;
    private StageController controller;

    public Server(int port, StageController controller) {
        this.port = port;
        this.controller = controller;
    }

    public Server(StageController controller) {
        this(9000, controller);
    }

    private void handleNetworkInput(Socket socket) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Gson gson = new Gson();
            MeshController meshController = createMeshController();

            String command;
            while ((command = reader.readLine()) != null) {
                TransformMessage msg = gson.fromJson(command, TransformMessage.class);

                if ("exit".equalsIgnoreCase(msg.getTransformUnit())) {
                    break;
                }

                handleTransformCommand(meshController, msg);
            }
        }
    }

    private MeshController createMeshController() {
        return new MeshController(
                this.controller.getMainSceneController()
                        .getMainScene()
                        .getModelSubScene()
                        .getMeshView()
        );
    }

    private void handleTransformCommand(MeshController meshController, TransformMessage msg) {
        switch (msg.getCommand().toLowerCase()) {
            case "rotate" ->
                    meshController.rotateBy(msg.getTransformUnit(), msg.getVal());
            case "translate" ->
                    meshController.moveBy(msg.getTransformUnit(), msg.getVal());
            default ->
                    System.err.println("Ungültiger Befehl: " + msg.getCommand());
        }
    }




    @Override
    public void run() {
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Warte auf Verbindungsanfrage …");
            while (true) {
                try (Socket client = ss.accept()) {   // accept blockiert bis ein Client kommt
                    System.out.println("Verbunden mit " + client.getInetAddress());
                    handleNetworkInput(client);

                } catch (IOException e) {
                    System.err.println("Client-Fehler: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server konnte nicht gestartet werden: " + e.getMessage());
        }
    }
}
