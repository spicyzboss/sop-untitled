package Client;

import com.proto.average.AverageGrpc;
import com.proto.maxscore.MaxScoreGrpc;
import com.proto.maxscore.MaxScoreRequest;
import com.proto.maxscore.MaxScoreResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MaxScoreClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        MaxScoreGrpc.MaxScoreStub asyncClient = MaxScoreGrpc.newStub(channel);

        StreamObserver<MaxScoreResponse> response = new StreamObserver<MaxScoreResponse>() {
            @Override
            public void onNext(MaxScoreResponse value) {
                System.out.println("Server To Client >> Max Score is " + value.getHighScore());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
                System.out.println("Server is Done");
            }
        };

        StreamObserver<MaxScoreRequest> stream = asyncClient.findMaxScore(response);

//        int[] data = {4, 0, 9, 10, 2, 3, 17, 5, 9, 14};
//
//        Iterator<Integer> it = Arrays.stream(data).iterator();
//
//        while (it.hasNext()) {
//            try {
//                latch.await(1L, TimeUnit.SECONDS);
//                Integer x = it.next();
//                System.out.println("Client To Server : " + x);
//                stream.onNext(MaxScoreRequest
//                        .newBuilder()
//                        .setScore(x)
//                        .build()
//                );
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        stream.onCompleted();
//        try {
//            latch.await(3L, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        channel.shutdown();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            int scoreInput = scanner.nextInt();

            if (scoreInput == -1){
                stream.onCompleted();
                try {
                    latch.await(3L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.shutdown();
            } else {
                MaxScoreRequest request = MaxScoreRequest
                        .newBuilder()
                        .setScore(scoreInput)
                        .build();
                System.out.println("Client To Server : " + request);
                stream.onNext(request);
            }
        }
    }
}
