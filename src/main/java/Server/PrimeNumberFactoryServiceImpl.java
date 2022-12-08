package Server;

import com.proto.prime.PrimeFactoryGrpc;
import com.proto.prime.PrimeFactoryRequest;
import com.proto.prime.PrimeFactoryResponse;
import io.grpc.stub.StreamObserver;

public class PrimeNumberFactoryServiceImpl extends PrimeFactoryGrpc.PrimeFactoryImplBase {
    @Override
    public void primeDecomposition(PrimeFactoryRequest primeFactoryRequest, StreamObserver<PrimeFactoryResponse> responseStreamObserver) {
        Integer number = primeFactoryRequest.getNumber();
        for (int i = 2; i< number; i++) {
            while (number%i == 0) {
                PrimeFactoryResponse response = PrimeFactoryResponse.newBuilder().setPrime(i).build();
                responseStreamObserver.onNext(response);
                number = number/i;
            }
        }

        if(number >2) {
            PrimeFactoryResponse response = PrimeFactoryResponse.newBuilder().setPrime(number).build();
            responseStreamObserver.onNext(response);
        }
    }
}
