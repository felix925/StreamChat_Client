package jp.making.streamchat.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import main.Chat

@ExperimentalCoroutinesApi
class ChatServiceImpl : ChatService {
    private val api: ChatApi = ChatApi()
    override fun flowChatMessage(request: Flow<Chat.ChatRequest>): Flow<Chat.ChatResponse> =
            channelFlow<Chat.ChatResponse> {
                withContext(Dispatchers.IO) {
                    val observer = api.observeChatMessage(
                            onNext = {
                                launch {
                                    channel.send(Chat.ChatResponse.newBuilder().setMessage(it).build())
                                }
                            },
                            onError = {
                                throw it
                            },
                            onCompleted = {
                                channel.close()
                            }
                    )
                    request.onEach {
                        val req = Chat.ChatRequest.newBuilder()
                                .setMessage(it.message)
                                .build()
                        observer.onNext(req)
                    }.launchIn(this)
                }
                awaitClose()
            }
                    .flowOn(Dispatchers.IO)
                    .buffer()
}