package com.example.chat_rober;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    // Área donde se muestran los mensajes del chat
    @FXML private TextArea Area_texto;
    // Campo donde el usuario escribe mensajes
    @FXML private TextField bara_texto;
    // Botón para enviar mensajes
    @FXML private Button btn_enviar;
    // Botón para abrir otra ventana de chat
    @FXML private Button new_user;
    // Botón para crear un nuevo caht
    @FXML private Button new_chat;

    // Cliente que maneja la conexión con el servidor
    private EchoClient cliente;

    @FXML
    // Metodo que se ejecuta al cargar el controlador
    public void initialize() {
        // Crear instancia del cliente de chat
        cliente = new EchoClient();

        // Crear un hilo independiente para conectarse al servidor y recibir mensajes
        new Thread(() -> {
            try {
                // Conectamos al servidor en localhost y puerto 8080
                cliente.conectar("localhost", 8080);

                // Mostrar mensaje de conexión en la vista usando Platform.runLater
                Platform.runLater(() -> Area_texto.appendText("✅ Conectado al servidor"+ "\n"));

                // Bucle para recibir mensajes continuamente del servidor
                String mensaje;
                while ((mensaje = cliente.recibir()) != null) {
                    String mensaje_final = mensaje; // Necesario para lambda
                    Platform.runLater(() ->
                            // Mostrar mensaje en la vista
                            Area_texto.appendText(mensaje_final + "\n")
                    );
                }

            } catch (IOException e) {
                // Si ocurre un error, mostrarlo en la vista
                Platform.runLater(() -> Area_texto.appendText("❌ Error: " + e.getMessage() + "\n"));
            }
        }).start(); // Inicia el hilo

        // Configuramos la función del botón enviar
        btn_enviar.setOnAction(e -> {
            // Leer el texto escrito del mensaje
            String texto = bara_texto.getText().trim();
            // Si el texo no esta vacio
            if (!texto.isEmpty()) {
                // Envialo al servidor
                cliente.enviar(texto);
                // Limpia la bara de texto
                bara_texto.clear();
            }
        });

        // Configuramos la función del botón nueva ventana
        new_user.setOnAction(e -> {
            try {
                // Crear nueva ventana
                Stage stage = new Stage();
                // Cargamos el FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                // Asignamos la escena y tamaño
                stage.setScene(new Scene(loader.load(), 390, 615));
                // Damos un título de la ventana
                stage.setTitle("Grupo Rober");
                // Mostramos la ventana
                stage.show();
            } catch (IOException ex) {
                // Mostrar errores si falla al cargar FXML
                ex.printStackTrace();
            }
        });

        // COnfiguración de la funcion del boton de nuevo chat
        new_chat.setOnAction(e->{
            try {
                //Creamos una nueva ventana
                Stage stage= new Stage();
                //Cargamos fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Opci_inicio.fxml"));
                // Asignamos la escena y tamaño
                stage.setScene(new Scene(loader.load(), 430, 400));
                // Damos un título de la ventana
                stage.setTitle("Grupo Rober");
                // Mostramos la ventana
                stage.show();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        });
    }
}

