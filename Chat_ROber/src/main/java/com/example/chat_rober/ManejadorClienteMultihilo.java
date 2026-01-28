package com.example.chat_rober;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ManejadorClienteMultihilo implements Runnable {
    // Socket asociado a este cliente
    private final Socket socket;
    // Número único asignado a este cliente
    private final int numeroCliente;
    // Constructor: recibe el socket del cliente y su número
    public ManejadorClienteMultihilo(Socket socket, int numeroCliente) {
        this.socket = socket;
        this.numeroCliente = numeroCliente;
    }

    // Creamos un metodo que se ejecuta cuando se corre el hilo
    public void run() {
        // Creamos una variable de salida para enviar mensajes al cliente
        PrintWriter salida = null;
        // Con un Try-catch controlamos el BufferedReader para leer mensajes del cliente
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Inicializamos el PrintWriter para enviar datos al cliente
            salida = new PrintWriter(socket.getOutputStream(), true);

            // Enviamos un mensaje de bienvenida al cliente
            salida.println("¡Bienvenido! Eres el cliente #" + numeroCliente);

            // Tras lo cual agregamos este cliente a la lista global de clientes conectados
            EchoServerMultihilo.lista_usuarios_en_linea.add(salida);

            // Envíamos a cada nuevo cliente todo el historial de mensajes guardados
            for (String cada_mensaje : EchoServerMultihilo.cloud_chat) {
                salida.println(cada_mensaje);
            }
            // Creamos una variable para almacenar cada mensaje recibido
            String mensaje;
            // Usando un bucle infinito mientras el cliente envíe mensajes
            while ((mensaje = entrada.readLine()) != null) {
                // Imprimimos en consola el mensaje recibido
                System.out.println("[Usuario #" + numeroCliente + "] " + mensaje);
                // Reenvíamos el mensaje a todos los clientes conectados
                for (PrintWriter cliente : EchoServerMultihilo.lista_usuarios_en_linea) {
                    cliente.println("[Usuario #" + numeroCliente + "] " + mensaje);
                }
                // Guardamos el mensaje en el historial global del chat
                EchoServerMultihilo.cloud_chat.add("[Usuario #" + numeroCliente + "] " + mensaje);
                // Si el mensaje es "salir", el cliente quiere desconectarse
                if (mensaje.equalsIgnoreCase("salir")) {
                    // Recorremos todos los clientes conectados
                    for (PrintWriter cliente : EchoServerMultihilo.lista_usuarios_en_linea) {
                        // Envíamos un mensaje de despedida
                        cliente.println("👋 Usuario #" + numeroCliente + " se ha desconectado");
                    }
                    // Quitamos el cliente del bucle y terminamos la comunicación con este cliente
                    break;
                }
            }
        } catch (IOException e) {
            // Captura errores de comunicación con el cliente
            System.err.println("Error con cliente #" + numeroCliente + ": " + e.getMessage());
        } finally {
            // Siempre que se finalice la ejecucion del hilo
            // Quitamos al cliente de la lista global para no siga enviándole mensajes
            if (salida != null) {
                EchoServerMultihilo.lista_usuarios_en_linea.remove(salida);
            }
            // Cierramos el socket del cliente
            try {
                socket.close();
                // Enviamos un mensaje de que se ha desconectado
                System.out.println("❌ Cliente #" + numeroCliente + " desconectado");
            } catch (IOException e) {
                // Si falla al cerrar, mostramos el mensaje del error
                e.printStackTrace();
            }
        }
    }
}

/*
Resumen de funcionamiento:
Cada cliente que se conecta genera un hilo independiente (Runnable).
Se maneja la comunicación bidireccional:
    Recibe mensajes del cliente (entrada.readLine())
    Los reenvía a todos los clientes conectados (PrintWriter)
Mantenemos un historial global (cloud_chat) para enviar los mensajes previos al nuevo cliente.
Permitimos la desconexión limpia cuando el cliente envía "salir".
*/