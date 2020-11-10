package jp.making.streamchat

import androidx.lifecycle.*
import jp.making.streamchat.api.ChatService
import jp.making.streamchat.api.ChatServiceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import main.Chat

@ExperimentalCoroutinesApi
class ChatViewModel : ViewModel() {
    private val service: ChatService = ChatServiceImpl()
    private val myMessage = MutableLiveData<Chat.ChatRequest>()
    val receiveMessage = service.flowChatMessage(myMessage.asFlow()).asLiveData()

    fun sendMessage(message: String): Job = viewModelScope.launch {
        myMessage.value = Chat.ChatRequest
                .newBuilder()
                .setMessage(message)
                .build()
    }
}