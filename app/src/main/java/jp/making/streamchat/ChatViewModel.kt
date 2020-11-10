package jp.making.streamchat

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import main.Chat

@ExperimentalCoroutinesApi
class ChatViewModel : ViewModel() {
    private val api = ChatApi()
    private val myMessage = MutableLiveData<Chat.ChatRequest>()
    val receiveMessage = flowChat(myMessage.asFlow()).asLiveData()
    private val _response = MutableLiveData<String>()
    val response
        get() = _response

    fun sendMessage(message: String): Job = viewModelScope.launch {
        myMessage.value = Chat.ChatRequest
                .newBuilder()
                .setMessage(message)
                .build()
    }

    private suspend fun setResponse(str: String) {
        _response.postValue(str)
    }

    private fun flowChat(
            request: Flow<Chat.ChatRequest>
    ): Flow<Chat.ChatResponse> = channelFlow<Chat.ChatResponse> {
        viewModelScope.launch(Dispatchers.IO) {
            val observer = api.observeChatMessage(
                    onNext = {
                        launch {
//                            channel.send(Chat.ChatResponse.newBuilder().setMessage(it).build())
                        }
                    },
                    onCompleted = {
                        channel.close()
                    },
                    onError = {
                        throw it
                    }
            )
            request.onEach {
                val req = Chat.ChatRequest.newBuilder()
                        .setMessage(it.message)
                        .build()
                observer.onNext(req)
            }.launchIn(this)
        }
    }
            .flowOn(Dispatchers.IO)
            .buffer()
}