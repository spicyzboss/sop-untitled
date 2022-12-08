package Client;

import com.proto.prime.PrimeFactoryGrpc;
import com.proto.prime.PrimeFactoryRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PrimeNumberFactoryClient {
    public static void main(String[] args) {
        System.out.println("Hello Prime Client");
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        System.out.println("Creating Stub...");
        PrimeFactoryGrpc.PrimeFactoryBlockingStub primeClient;
        primeClient = PrimeFactoryGrpc.newBlockingStub(channel);
        System.out.println("Stub Created");

        PrimeFactoryRequest primeRequest = PrimeFactoryRequest
                .newBuilder()
                .setNumber(108)
                .build();
        System.out.println("Client To Server : " + primeRequest.getNumber());

        primeClient.primeDecomposition(primeRequest).forEachRemaining(response -> {
            System.out.println("Server To Client : " + response.getPrime());
        });
        channel.shutdown();
    }
}
