package com.example.chat_rober;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

// Clase principal de la aplicación, hereda de Application
public class HelloApplication extends Application {

    // Metodo que se ejecuta al iniciar la aplicación
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Llama a metodo para crear una nueva ventana de usuario
        new_user();
    }

    // Metodo que crea una nueva ventana de chat
    public void new_user() throws IOException {
        // Crear una nueva ventana independiente
        Stage stage = new Stage();
        // Cargar diseño FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        // Crear la escena con ancho y alto
        Scene scene = new Scene(fxmlLoader.load(), 390, 615);
        // Título de la ventana
        stage.setTitle("Grupo Rober");
        // Asignar la escena a la ventana
        stage.setScene(scene);
        // Mostrar la ventana
        stage.show();
    }

    // Metodo main, punto de entrada del programa
    public static void main(String[] args) {
        // Lanza la aplicación JavaFX (llama automáticamente a start())
        launch();
    }
}
