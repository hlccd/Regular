package hlccd.regular.Bill;
/**
 * 账单记录修改活动
 * 接受传过来的账单记录并找到对应记录
 * 可进行修改或删除该记录的操作
 *
 * @author hlccd 2020.6.7
 * @version 1.0
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hlccd.regular.util.GV_adapter;
import hlccd.regular.util.GV_data;
import hlccd.regular.R;

public class Bill_record_edit extends AppCompatActivity {
    private Bill_record_data data;
    private ImageView        image;
    private TextView         money;
    private Button           consume;
    private Button           income;
    private GridView         grid;
    private TextView         timestamp;
    private TextView         remark;
    private List<GV_data>    images;
    private int              number = 0;
    private String           type   = "消费";

    //计算器中的控件
    private String    num   = "";
    private int       digit = -1;
    private TextView  one;
    private TextView  two;
    private TextView  three;
    private TextView  four;
    private TextView  five;
    private TextView  six;
    private TextView  seven;
    private TextView  eight;
    private TextView  nine;
    private TextView  AC;
    private TextView  zero;
    private TextView  dot;
    private ImageView delete;
    private ImageView rollback;
    private TextView  finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_record_edit);
        Intent intent = getIntent();
        String s      = ((Intent) intent).getStringExtra("Bill_record_data");
        data = LitePal.where("timestamp=?", s).find(Bill_record_data.class).get(0);

        image     = findViewById(R.id.bill_record_edit_image);
        money     = findViewById(R.id.bill_record_edit_money);
        consume   = findViewById(R.id.bill_record_edit_consume);
        income    = findViewById(R.id.bill_record_edit_income);
        grid      = findViewById(R.id.bill_record_edit_gridview);
        timestamp = findViewById(R.id.bill_record_edit_timestamp);
        remark    = findViewById(R.id.bill_record_edit_remark);
        calculator(10, 2);
        initialize();
        //收支按键点击更改背景和数据类型
        consume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images = new GV_data().ToGV(new Bill_type_second_data().consume());
                GV_adapter adapter = new GV_adapter(view.getContext(), images);
                grid.setAdapter(adapter);
                type   = "消费";
                number = 0;
                image.setImageResource(images.get(number).getImage());
                consume.setBackgroundResource(R.drawable.main_circle_bead);
                income.setBackgroundResource(R.color.background);
            }
        });
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                images = new GV_data().ToGV(new Bill_type_second_data().income());
                GV_adapter adapter = new GV_adapter(view.getContext(), images);
                grid.setAdapter(adapter);
                type   = "收入";
                number = 0;
                image.setImageResource(images.get(number).getImage());
                income.setBackgroundResource(R.drawable.main_circle_bead);
                consume.setBackgroundResource(R.color.background);
            }
        });
        //点击grid中图片更改保存时的收支类型
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = position;
                image.setImageResource(images.get(number).getImage());
            }
        });
    }

    //初始化,根据接受的record_data数据的类型修改初始展示状态
    private void initialize() {
        num = String.valueOf(data.getMoney());
        for (int i = 0; i < num.length(); i++) {
            if (num.charAt(i) == '.' || digit >= 0) {
                digit++;
            }
        }
        //根据获取的data展示消费/收入情况,同时修改gridView中的参数
        type = data.getType();
        if (data.getType().equals("消费")) {
            images = new GV_data().ToGV(new Bill_type_second_data().consume());
            consume.setBackgroundResource(R.drawable.main_circle_bead);
            income.setBackgroundResource(R.color.background);
        } else {
            images = new GV_data().ToGV(new Bill_type_second_data().income());
            income.setBackgroundResource(R.drawable.main_circle_bead);
            consume.setBackgroundResource(R.color.background);
        }
        //修改展示图标
        image.setImageResource(images.get(number).getImage());
        money.setText(String.valueOf(data.getMoney()));
        GV_adapter adapter = new GV_adapter(this, images);
        grid.setAdapter(adapter);
        //展示该笔账务记录的时刻
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timestamp.setText(simpleDateFormat.format(new Date(data.getTimestamp())));
        remark.setText(data.getRemark());
    }

    private void calculator(int integral, int decimal) {
        //对接计算器中的控件
        //点击删除即可将该笔记录进行删除操作
        one      = findViewById(R.id.one);
        two      = findViewById(R.id.two);
        three    = findViewById(R.id.three);
        four     = findViewById(R.id.four);
        five     = findViewById(R.id.five);
        six      = findViewById(R.id.six);
        seven    = findViewById(R.id.seven);
        eight    = findViewById(R.id.eight);
        nine     = findViewById(R.id.nine);
        AC       = findViewById(R.id.AC);
        zero     = findViewById(R.id.zero);
        dot      = findViewById(R.id.dot);
        delete   = findViewById(R.id.delete);
        rollback = findViewById(R.id.rollback);
        finish   = findViewById(R.id.finish);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "1";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "1";
                    money.setText(num);
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "2";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "2";
                    money.setText(num);
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "3";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "3";
                    money.setText(num);
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "4";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "4";
                    money.setText(num);
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "5";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "5";
                    money.setText(num);
                }
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "6";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "6";
                    money.setText(num);
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "7";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "7";
                    money.setText(num);
                }
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "8";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "8";
                    money.setText(num);
                }
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "9";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "9";
                    money.setText(num);
                }
            }
        });
        AC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num   = "";
                digit = -1;
                money.setText("00.0");
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    if (num.length() < integral) {
                        num += "0";
                        money.setText(num);
                    }
                } else if (digit < decimal) {
                    digit++;
                    num += "0";
                    money.setText(num);
                }
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (digit < 0) {
                    digit = 0;
                    num += ".";
                    money.setText(num);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.delete();
                finish();
            }
        });
        rollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num.length() > 0) {
                    if (num.charAt(num.length() - 1) == '.') {
                        digit = -1;
                    }
                    num = num.substring(0, num.length() - 1);
                    money.setText(num);
                    if(num.length()==0){
                        money.setText("00.0");
                    }
                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num.length() > 0 && Double.valueOf(num)>=0.01) {
                    data.setMoney(Double.parseDouble(money.getText().toString()));
                    data.setCategory(images.get(number).getDescribe());
                    data.setType(type);
                    data.setRemark(remark.getText().toString());
                    data.setTimestamp(System.currentTimeMillis());
                    data.save();
                    finish();
                }
            }
        });
    }
}