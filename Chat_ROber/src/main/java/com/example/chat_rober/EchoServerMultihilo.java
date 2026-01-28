package com.example.chat_rober;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EchoServerMultihilo {
    // Declaramos el puerto en el que el servidor escuchará conexiones
    private static final int PUERTO = 8080;
    // Declaramos el número máximo de clientes que el servidor manejará simultáneamente
    private static final int MAX_CLIENTES = 10;

    // AtomicInteger: Variable segura para hilos que cuenta los clientes conectados
    private static final AtomicInteger clientesConectados = new AtomicInteger(0);


    // Creamos una lista de los streams de salida de cada usuario conectado, para enviar mensajes
    public static List<PrintWriter> lista_usuarios_en_linea = new CopyOnWriteArrayList<>();
    // Creamos otra lista que guarda todos los mensajes enviados por los usuarios (historial del chat)
    public static List<String> cloud_chat = new CopyOnWriteArrayList<>();


    public static void main(String[] args) {
        // Crea un pool de hilos fijo para manejar hasta MAX_CLIENTES simultáneamente
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES);

        System.out.println("Servidor multihilo iniciado en puerto " + PUERTO);
        System.out.println("📊 Pool de hilos: " + MAX_CLIENTES);

        // Creamos un try-catch para asegurar que el ServerSocket se cierre correctamente
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            // Mientras el servido este activo, creamos un bucle infinito para aceptar clientes continuamente
            while (true) {
                // Esperamos hasta que un cliente se conecte
                Socket clienteSocket = serverSocket.accept();
                // Cuando se conecte, incrementamos el número y obtenemos el número de cliente
                int numCliente = clientesConectados.incrementAndGet();
                // Muestramos la IP del cliente conectado
                System.out.println("✅ Cliente #" + numCliente + " conectado: " + clienteSocket.getInetAddress());
                // Envíamos la tarea de manejar al cliente al pool de hilos
                pool.execute(new ManejadorClienteMultihilo(clienteSocket, numCliente));
            }
        } catch (IOException e) {
            // Captura errores de red o problemas al crear el ServerSocket
            System.err.println("Error en servidor: " + e.getMessage());
        } finally {
            // Cierra el pool de hilos cuando termina el servidor
            pool.shutdown();
        }
    }
}
