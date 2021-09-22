package hlccd.regular.Bill;
/**
 * 账务记录适配器
 * 该适配器主要为了适配每一条账务记录
 * 并根据该条记录的收支情况进行区别展示
 *
 * @author hlccd 2020.6.6
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import hlccd.regular.R;

public class Bill_record_data_adapter extends RecyclerView.Adapter<Bill_record_data_adapter.ViewHolder> {
    public List<Bill_record_data> records;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  income;
        TextView  expend;
        ImageView icon;

        public ViewHolder(View view) {
            super(view);
            //适配界面中的控件
            income = view.findViewById(R.id.bill_record_data_income);
            expend = view.findViewById(R.id.bill_record_data_consume);
            icon   = view.findViewById(R.id.bill_record_data_icon);
        }
    }

    public Bill_record_data_adapter(List<Bill_record_data> records) {
        this.records = records;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_record_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //从该日的账务记录中取出某一条记录
        Bill_record_data record = records.get(position);
        if (record.getType().equals("收入")) {
            //如果该条记录属于收入类型则展示在左侧
            holder.icon.setImageResource(R.drawable.main_plus);
            holder.income.setText(record.getCategory() + record.getMoney());
            holder.income.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    to_edit(view, record);
                }
            });
        } else if (record.getType().equals("消费")) {
            //如果该条记录属于收入类型则展示在左侧
            holder.icon.setImageResource(R.drawable.main_minus);
            holder.expend.setText(record.getCategory() + record.getMoney());
            holder.expend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    to_edit(view, record);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    //    向编辑活动发送数据并跳转过去
    private void to_edit(View view, Bill_record_data data) {
        Intent intent = new Intent();
        intent.setClass(view.getContext(), Bill_record_edit.class);
        intent.putExtra("Bill_record_data", String.valueOf(data.getTimestamp()));
        view.getContext().startActivity(intent);
    }
}
