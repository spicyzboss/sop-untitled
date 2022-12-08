package Client;

import com.proto.average.AverageGrpc;
import com.proto.average.AverageRequest;
import com.proto.average.AverageResponse;
import com.proto.prime.PrimeFactoryGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AverageClient {
    public static void main(String[] args) {
        System.out.println("Hello Average Client");
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();
        CountDownLatch latch = new CountDownLatch(1);

        System.out.println("Creating Stub...");
        AverageGrpc.AverageStub averageClient = AverageGrpc.newStub(channel);
        System.out.println("Stub Created");

        System.out.println("Creating Response Stream...");
        StreamObserver<AverageResponse> response = new StreamObserver<AverageResponse>() {
            @Override
            public void onNext(AverageResponse value) {
                System.out.println("Average is " + value.getAverageOutput());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
                System.out.println("Completed sending from Server");
            }
        };
        System.out.println("Created Response Stream");

        System.out.println("Creating Request...");
        StreamObserver<AverageRequest> stream = averageClient.computeAverage(response);

        for (int i = 1; i <= 10; i++) {
            stream.onNext(AverageRequest
                .newBuilder()
                .setNumber(i)
                .build()
            );
        }
        stream.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Ended");
    }
}
