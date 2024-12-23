import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import stub.SalutationGrpc;
import stub.SalutationOuterClass;

import java.io.IOException;

public class MessageService extends SalutationGrpc.SalutationImplBase {
    @Override
    public StreamObserver<SalutationOuterClass.SalutRequest> message(StreamObserver<SalutationOuterClass.SalutResponse> responseObserver) {
        return
        new StreamObserver<>() {
            StringBuilder names = new StringBuilder();
            @Override
            public void onNext(SalutationOuterClass.SalutRequest request) {
// Traite chaque message envoyé par le client
                names.append(request.getName()).append(", ");
            }
            @Override
            public void onError(Throwable t) {
                System.err.println("Erreur reçue : " + t.getMessage());
            }
            @Override
            public void onCompleted() {
// Envoie une réponse au client une fois que tous les messages sont reçus
                String responseMessage = "Salut à tous : " + names.toString();
                SalutationOuterClass.SalutResponse response = SalutationOuterClass.SalutResponse.newBuilder()
                        .setMessage(responseMessage)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }


        };

    }public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new MessageService())
                .build();
        server.start();
        System.out.println("Serveur gRPC en cours d'exécution sur le port "+server.getPort());
        server.awaitTermination();
    }
}