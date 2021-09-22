package hlccd.regular.Bill;
/**
 * 按日期分割的账务记录适配器
 * 该适配器主要为了适配不同日期的账务记录
 * 对该日的记录进行计算得到该日整体的收支情况
 * 下属该日的每一笔账务记录
 *
 * @author hlccd 2020.6.6
 * @version 1.0
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hlccd.regular.R;

public class Bill_record_RV_adapter extends RecyclerView.Adapter<Bill_record_RV_adapter.ViewHolder> {
    public Context                      context;
    public List<List<Bill_record_data>> records_s;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView     left;
        TextView     right;
        ImageView    point;
        RecyclerView recycler;

        public ViewHolder(View view) {
            super(view);
            //适配界面中的控件
            left     = view.findViewById(R.id.bill_record_RV_left);
            right    = view.findViewById(R.id.bill_record_RV_right);
            point    = view.findViewById(R.id.bill_record_RV_point);
            recycler = view.findViewById(R.id.bill_record_RVRV);
        }
    }

    public Bill_record_RV_adapter(Context context, List<List<Bill_record_data>> records_s) {
        this.context   = context;
        this.records_s = records_s;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_record_rv, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint ("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //从全部的该用户的数据中找出其中某一日的账务记录
        List<Bill_record_data> records = records_s.get(position);
        if (!records.isEmpty()) {
            //当该日的账务记录不为空的时候即可进行统计该日的收支情况
            double num = 0;
            for (Bill_record_data data : records) {
                if (data.getType().equals("收入")) {
                    num += data.getMoney();
                } else if (data.getType().equals("消费")) {
                    num -= data.getMoney();
                }
            }
            //根据该日的记录查询该组记录的具体日期用以显示
            SimpleDateFormat date_forma = new SimpleDateFormat("yyyy-MM-dd");
            String           date       = date_forma.format(new Date(records.get(0).getTimestamp()));
            if (num >= 0) {
                //当整体收入更多时候该日记录为汉白玉色基调,收入处左侧,日期处右侧
                holder.point.setImageResource(R.drawable.main_point);
                holder.left.setText(String.valueOf(num));
                holder.right.setText(date);
                holder.left.setTextColor(context.getResources().getColor(R.color.mainColor));
                holder.right.setTextColor(context.getResources().getColor(R.color.mainColor));
            } else {
                //当整体支出更多时候该日记录为瀑布蓝色基调,日期处左侧,支出处右侧
                holder.point.setImageResource(R.drawable.secondary_point);
                holder.right.setText(String.valueOf(num));
                holder.left.setText(date);
                holder.left.setTextColor(context.getResources().getColor(R.color.secondaryColor));
                holder.right.setTextColor(context.getResources().getColor(R.color.secondaryColor));
            }
            //将本日详细的账务记录数据传入下一层适配器中,用以展示本日的所有记录
            LinearLayoutManager      layoutManager = new LinearLayoutManager(context);
            Bill_record_data_adapter adapter       = new Bill_record_data_adapter(records);
            holder.recycler.setLayoutManager(layoutManager);
            holder.recycler.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return records_s.size();
    }
}

