package hlccd.regular.Friend;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hlccd.regular.API.IM;
import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.User.User;

public class Friend_info_data_adapter extends RecyclerView.Adapter<Friend_info_data_adapter.ViewHolder> {
    public List<Friend_info_data> infos;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView        portrait;
        TextView         nameId;
        TextView         mes;
        TextView         consent;
        TextView         reject;

        public ViewHolder(View view) {
            super(view);
            layout   = view.findViewById(R.id.layout);
            portrait = view.findViewById(R.id.portrait);
            nameId   = view.findViewById(R.id.nameId);
            mes      = view.findViewById(R.id.mes);
            consent  = view.findViewById(R.id.consent);
            reject   = view.findViewById(R.id.reject);
        }
    }

    public Friend_info_data_adapter(List<Friend_info_data> infos) {
        this.infos = infos;
    }

    @Override
    public Friend_info_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_info_data, parent, false);
        return new Friend_info_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Friend_info_data_adapter.ViewHolder holder, int position) {
        //获取其中一条个人习惯记录
        Friend_info_data info = infos.get(position);
        holder.portrait.setImageResource(info.getPortrait());
        switch (info.getType()) {
            case 3:
                makeFriend(holder, info);
                break;
            case 4:
                joinGroup(holder, info);
                break;
            case 5:
                consentAdd(holder, info);
                break;
            case 6:
                consentJoin(holder, info);
                break;
            case 7:
                rejectAdd(holder, info);
                break;
            case 8:
                rejectJoin(holder, info);
                break;
        }
    }

    private void makeFriend(Friend_info_data_adapter.ViewHolder holder, Friend_info_data info) {
        holder.nameId.setText(info.getName() + "(" + info.getSender() + ")");
        holder.mes.setText("对方想要添加您为好友");
        if (info.getDispose()) {
            holder.consent.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
        } else {
            holder.consent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("mes", "");
                                json.put("id", User.getUserId());
                                json.put("receiver", info.getSender());
                                json.put("type", 5);
                                json.put("group", info.getGroup());
                                json.put("timestamp", System.currentTimeMillis());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!IM.getSocket().isClosed()) {
                                IM.getWriter().println(json);
                                IM.getWriter().flush();
                                info.setDispose(true);
                                info.save();
                                holder.consent.setVisibility(View.GONE);
                                holder.reject.setVisibility(View.GONE);
                            }
                        }
                    }).start();
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("mes", "");
                                json.put("id", User.getUserId());
                                json.put("receiver", info.getSender());
                                json.put("type", 7);
                                json.put("group", info.getGroup());
                                json.put("timestamp", System.currentTimeMillis());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!IM.getSocket().isClosed()) {
                                IM.getWriter().println(json);
                                IM.getWriter().flush();
                                info.setDispose(true);
                                info.save();
                                holder.consent.setVisibility(View.GONE);
                                holder.reject.setVisibility(View.GONE);
                            }
                        }
                    }).start();
                }
            });
        }
    }

    private void joinGroup(Friend_info_data_adapter.ViewHolder holder, Friend_info_data info) {
        holder.nameId.setText(info.getName() + "(" + info.getSender() + ")");
        holder.mes.setText("该成员想加入群:"+info.getGroup());
        if (info.getDispose()) {
            holder.consent.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
        } else {
            holder.consent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("mes", "");
                                json.put("id", User.getUserId());
                                json.put("receiver", info.getSender());
                                json.put("type", 6);
                                json.put("group", info.getGroup());
                                json.put("timestamp", System.currentTimeMillis());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!IM.getSocket().isClosed()) {
                                IM.getWriter().println(json);
                                IM.getWriter().flush();
                                info.setDispose(true);
                                info.save();
                                holder.consent.setVisibility(View.GONE);
                                holder.reject.setVisibility(View.GONE);
                            }
                        }
                    }).start();
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("mes", "");
                                json.put("id", User.getUserId());
                                json.put("receiver", info.getSender());
                                json.put("type", 8);
                                json.put("group", info.getGroup());
                                json.put("timestamp", System.currentTimeMillis());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (!IM.getSocket().isClosed()) {
                                IM.getWriter().println(json);
                                IM.getWriter().flush();
                                info.setDispose(true);
                                info.save();
                                holder.consent.setVisibility(View.GONE);
                                holder.reject.setVisibility(View.GONE);
                            }
                        }
                    }).start();
                }
            });
        }
    }

    private void consentAdd(Friend_info_data_adapter.ViewHolder holder, Friend_info_data info) {
        holder.nameId.setText(info.getName() + "(" + info.getSender() + ")");
        holder.mes.setText("对方同意添加您为好友");
        holder.consent.setVisibility(View.GONE);
        holder.reject.setVisibility(View.GONE);
        info.setDispose(true);
        info.save();
    }

    private void consentJoin(Friend_info_data_adapter.ViewHolder holder, Friend_info_data info) {
        holder.nameId.setText(info.getName() + "(" + info.getGroup() + ")");
        holder.mes.setText("群管理同意您入群");
        holder.consent.setVisibility(View.GONE);
        holder.reject.setVisibility(View.GONE);
        info.setDispose(true);
        info.save();
    }

    private void rejectAdd(Friend_info_data_adapter.ViewHolder holder, Friend_info_data info) {
        holder.nameId.setText(info.getName() + "(" + info.getSender() + ")");
        holder.mes.setText("对方拒绝添加您为好友");
        holder.consent.setVisibility(View.GONE);
        holder.reject.setVisibility(View.GONE);
        info.setDispose(true);
        info.save();
    }

    private void rejectJoin(Friend_info_data_adapter.ViewHolder holder, Friend_info_data info) {
        holder.nameId.setText(info.getName() + "(" + info.getGroup() + ")");
        holder.mes.setText("群管理拒绝您入群");
        holder.consent.setVisibility(View.GONE);
        holder.reject.setVisibility(View.GONE);
        info.setDispose(true);
        info.save();
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }
}
