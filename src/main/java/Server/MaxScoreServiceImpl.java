package Server;

import com.proto.maxscore.MaxScoreGrpc;
import com.proto.maxscore.MaxScoreRequest;
import com.proto.maxscore.MaxScoreResponse;
import io.grpc.stub.StreamObserver;

public class MaxScoreServiceImpl extends MaxScoreGrpc.MaxScoreImplBase {
    private int maxNumber = 0;

    @Override
    public StreamObserver<MaxScoreRequest> findMaxScore(StreamObserver<MaxScoreResponse> responseObserver) {
        return new StreamObserver<MaxScoreRequest>() {
            @Override
            public void onNext(MaxScoreRequest value) {
                maxNumber = Math.max(value.getScore(), maxNumber);
                MaxScoreResponse response = MaxScoreResponse
                        .newBuilder()
                        .setHighScore(maxNumber)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
