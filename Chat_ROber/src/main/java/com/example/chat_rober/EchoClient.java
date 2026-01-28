package com.example.chat_rober;

import java.io.*;
import java.net.Socket;

public class EchoClient {

    // Socket que conecta al cliente con el servidor
    private Socket socket;
    // Stream para leer mensajes recibidos desde el servidor
    private BufferedReader entrada;
    // Stream para enviar mensajes al servidor
    private PrintWriter salida;

    // Metodo para conectar al servidor dado host y puerto
    public void conectar(String host, int puerto) throws IOException {
        // Crea el socket y se conecta al servidor
        socket = new Socket(host, puerto);
        // Inicializa flujo de lectura
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Inicializa flujo de escritura con autoflush
        salida = new PrintWriter(socket.getOutputStream(), true);
    }

    // Metodo para enviar un mensaje al servidor
    public void enviar(String mensaje) {
        // Comprueba que el flujo de salida está inicializado
        if (salida != null) {
            // Envía el mensaje al servidor
            salida.println(mensaje);
        }/* else {
            System.err.println("⚠️ No conectado: mensaje no enviado");
        }*/
    }

    // Metodo para recibir un mensaje del servidor
    public String recibir() throws IOException {
        // Comprueba que el flujo de entrada está inicializado
        if (entrada != null) {
            // Lee y devuelve una línea enviada por el servidor
            return entrada.readLine();
        }
        // Retorna null si no hay flujo de entrada
        return null;
    }
}

/*
Resumen del funcionamiento del cliente (EchoClient):
El metodo Conectar:
    crea un Socket hacia el servidor y abre los flujos de entrada y salida.
El metodo Enviar:
    manda cualquier mensaje al servidor usando PrintWriter.
El metodo Recibir:
    lee los mensajes del servidor de forma bloqueante (readLine()), uno a uno.
En resumen, esta clase solo maneja la lógica de la comunicación
*/