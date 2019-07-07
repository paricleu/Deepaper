package com.hackathon.deepaper.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hackathon.deepaper.Constants
import com.hackathon.deepaper.R
import com.hackathon.deepaper.model.Message
import com.hackathon.deepaper.show
import kotlinx.android.synthetic.main.item_message_received.view.*
import kotlinx.android.synthetic.main.item_message_sent.view.text_message_body


class MessageAdapter(
    private val mMessageList: List<Message>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view: View

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            return SentMessageHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            return ReceivedMessageHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = mMessageList[position]

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = mMessageList[position]

        return if (message.sender.userId == Constants.ME.userId) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    open class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private inner class SentMessageHolder(itemView: View) : MessageViewHolder(itemView) {

        fun bind(message: Message) = with(itemView) {
            text_message_body.text = message.msg
            text_message_time.text = message.createdAt
        }
    }

    private inner class ReceivedMessageHolder(itemView: View) : MessageViewHolder(itemView) {

        internal fun bind(message: Message) = with(itemView) {
            text_message_body.text = message.msg
            text_message_name.text = message.sender.nickname
            text_message_time.text = message.createdAt

            if (message.msg.contains("schönes Video")) {
                image_msg.show()
            }

//            // Insert the profile image from the URL into the ImageView.
//            Utils.displayRoundImageFromUrl(
//                mContext,
//                message.getSender().getProfileUrl(),
//                profileImage
//            )
        }
    }

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }
}