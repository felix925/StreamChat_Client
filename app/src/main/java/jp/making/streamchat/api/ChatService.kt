package jp.making.streamchat.api

import kotlinx.coroutines.flow.Flow
import main.Chat

interface ChatService {
    fun flowChatMessage(request: Flow<Chat.ChatRequest>): Flow<Chat.ChatResponse>
}