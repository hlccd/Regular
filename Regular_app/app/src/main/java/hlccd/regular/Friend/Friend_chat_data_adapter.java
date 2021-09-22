package hlccd.regular.Friend;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import hlccd.regular.Homepage;
import hlccd.regular.R;

public class Friend_chat_data_adapter extends RecyclerView.Adapter<Friend_chat_data_adapter.ViewHolder> {
    public List<Friend_chat_data> chats;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout other_layout;
        ImageView        other_portrait;
        TextView         other_name;
        TextView         other_mes;
        ConstraintLayout my_layout;
        ImageView        my_portrait;
        TextView         my_name;
        TextView         my_mes;

        public ViewHolder(View view) {
            super(view);
            other_layout   = view.findViewById(R.id.friend_message_other_layout);
            other_portrait = view.findViewById(R.id.friend_chat_other_portrait);
            other_name     = view.findViewById(R.id.friend_chat_other_name);
            other_mes      = view.findViewById(R.id.friend_chat_other_mes);
            my_layout   = view.findViewById(R.id.friend_message_my_layout);
            my_portrait = view.findViewById(R.id.friend_chat_my_portrait);
            my_name     = view.findViewById(R.id.friend_chat_my_name);
            my_mes      = view.findViewById(R.id.friend_chat_my_mes);
        }
    }

    public Friend_chat_data_adapter(List<Friend_chat_data> chats) {
        this.chats = chats;
    }

    @Override
    public Friend_chat_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_chat_data, parent, false);
        return new Friend_chat_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Friend_chat_data_adapter.ViewHolder holder, int position) {
        Friend_chat_data chat = chats.get(position);
        if (Objects.equals(chat.getSender(), chat.getMaster())){
            holder.other_layout.setVisibility(View.GONE);
            holder.my_name.setText(chat.getName());
            holder.my_portrait.setImageResource(chat.getPortrait());
            holder.my_mes.setText(chat.getMessage());
        }else{
            holder.my_layout.setVisibility(View.GONE);
            holder.other_name.setText(chat.getName());
            holder.other_portrait.setImageResource(chat.getPortrait());
            holder.other_mes.setText(chat.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}