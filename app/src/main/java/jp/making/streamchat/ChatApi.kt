package jp.making.streamchat

import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import main.Chat
import main.ChatServiceGrpc

class ChatApi {
    private val HOST = "10.0.2.2"
    private val PORT = 50051
    private val channel = ManagedChannelBuilder
            .forAddress(HOST, PORT)
            .usePlaintext()
            .build()
    private val stub = ChatServiceGrpc.newStub(channel)

    fun observeChatMessage(
            onNext: (String) -> Unit,
            onError: (Throwable) -> Unit,
            onCompleted: () -> Unit
    ): StreamObserver<Chat.ChatRequest> {
        return stub.chat(object : StreamObserver<Chat.ChatResponse> {
            override fun onNext(message: Chat.ChatResponse) {
                onNext(message.message)
            }

            override fun onError(t: Throwable?) {
                t?.let(onError)
            }

            override fun onCompleted() {
                onCompleted.invoke()
            }
        })
    }
}