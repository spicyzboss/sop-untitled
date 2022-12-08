package Server;

import com.proto.average.AverageGrpc;
import com.proto.average.AverageRequest;
import com.proto.average.AverageResponse;
import io.grpc.stub.StreamObserver;

public class AverageServiceImpl extends AverageGrpc.AverageImplBase {
    private int sumNumber = 0;
    private int messageCount = 0;

    @Override
    public StreamObserver<AverageRequest> computeAverage(StreamObserver<AverageResponse> responseObserver) {
        return new StreamObserver<AverageRequest>() {
            @Override
            public void onNext(AverageRequest value) {
                sumNumber += value.getNumber();
                messageCount++;
                System.out.println("Message No." + messageCount + " From Client >> Number is " + value.getNumber());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                double result = 0;
                result = (double) sumNumber / (double) messageCount;

                AverageResponse response = AverageResponse
                        .newBuilder()
                        .setAverageOutput(result)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                System.out.println("Finish");
            }
        };
    }
}
