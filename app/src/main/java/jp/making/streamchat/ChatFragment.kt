package jp.making.streamchat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import jp.making.streamchat.databinding.FragmentChatBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ChatFragment : Fragment(R.layout.fragment_chat) {
    private val viewModel: ChatViewModel by viewModels()
    lateinit var binding: FragmentChatBinding

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        binding.fab.setOnClickListener {
            viewModel.sendMessage("ヤッホー！！！")
        }
        viewModel.receiveMessage.observe(viewLifecycleOwner) {}
    }
}