package org.ea.utiltities;

import com.google.gson.Gson;
import org.ea.controller.MeshController;
import org.ea.controller.StageController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple TCP server that receives JSON-encoded transformation commands
 * and delegates them to a mesh controller for 3D model manipulation.
 *
 * @precondition The given StageController must be initialized with a valid scene.
 * @postcondition The server listens for incoming connections and executes transform commands.
 */
public class Server implements Runnable {

    private int port;
    private StageController controller;

    /**
     * Creates a server with the specified port and stage controller.
     *
     * @param port the port number to listen on
     * @param controller the application's stage controller
     * @precondition port > 0 && controller != null
     * @postcondition The server is ready to accept socket connections
     */
    public Server(int port, StageController controller) {
        this.port = port;
        this.controller = controller;
    }

    /**
     * Creates a server using the default port (9000).
     *
     * @param controller the application's stage controller
     * @precondition controller != null
     * @postcondition The server is initialized with port 9000
     */
    public Server(StageController controller) {
        this(9000, controller);
    }

    /**
     * Handles input from a connected client socket. Messages must be JSON-encoded.
     *
     * @param socket the connected client socket
     * @throws IOException if an I/O error occurs
     * @precondition socket != null and connected
     * @postcondition Processes and executes transformation commands
     */
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

    /**
     * Creates a mesh controller based on the current scene managed by the stage controller.
     *
     * @return a {@link MeshController} instance tied to the current mesh view
     * @precondition controller is initialized with a valid scene
     * @postcondition A mesh controller is returned for transformation operations
     */
    private MeshController createMeshController() {
        return new MeshController(
                this.controller.getMainSceneController()
                        .getMainScene()
                        .getModelSubScene()
                        .getMeshView()
        );
    }

    /**
     * Interprets and delegates a transformation command to the mesh controller.
     *
     * @param meshController the controller to apply transformations
     * @param msg the received transformation message
     * @precondition msg != null and contains valid command
     * @postcondition The corresponding transformation is applied
     */
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

    /**
     * Starts the server and continuously listens for incoming client connections.
     *
     * @precondition Server is properly configured and port is available
     * @postcondition Accepts client connections and processes incoming messages
     */
    @Override
    public void run() {
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Warte auf Verbindungsanfrage …");
            while (true) {
                try (Socket client = ss.accept()) {
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
