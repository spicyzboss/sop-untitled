package Server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class MaxScoreService {
    public static void main(String[] args) {
        Server server = ServerBuilder
                .forPort(50052)
                .addService(new MaxScoreServiceImpl())
                .build();

        try {
            server.start();
            System.out.println("Max Score Service Started");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(
            () -> {
                System.out.println("Received Shutdown Request");
                server.shutdown();
                System.out.println("Successfully Shutdown Server");
            }
        ));

        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
