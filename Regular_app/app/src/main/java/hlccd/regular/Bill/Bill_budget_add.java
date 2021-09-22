package hlccd.regular.Bill;

/**
 * 新增月度预算活动
 * 上方的符号区和消费类型组中选中项的图片进行绑定
 * 同时上方金额栏和下方计算器键盘进行绑定
 * 金额限定为10位整数2位小数
 * 该预算类型自选,时间锁定为所在机器当前年月
 * 如果同年月中出现重复类型,进行预算增加
 *
 * @author hlccd 2020.6.10
 * @version 1.0
 */


import androidx.appcompat.app.AppCompatActivity;

import hlccd.regular.util.GV_adapter;
import hlccd.regular.util.GV_data;
import hlccd.regular.R;
import hlccd.regular.User.User;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.List;

public class Bill_budget_add extends AppCompatActivity {
    private ImageView     image;
    private GridView      GV;
    private List<GV_data> consume = new GV_data().ToGV(new Bill_type_second_data().consume());
    private TextView      money;
    private View          view;
    private int           number  = 0;
    //计算器中的控件
    private String        num     = "";
    private int           digit   = -1;
    private TextView      one;
    private TextView      two;
    private TextView      three;
    private TextView      four;
    private TextView      five;
    private TextView      six;
    private TextView      seven;
    private TextView      eight;
    private TextView      nine;
    private TextView      AC;
    private TextView      zero;
    private TextView      dot;
    private ImageView     delete;
    private ImageView     rollback;
    private TextView      finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_budget_add);
        image = findViewById(R.id.bill_budget_add_image);
        GV    = findViewById(R.id.bill_budget_add_GV);
        money = findViewById(R.id.bill_budget_add_money);
        GV_data imageData = consume.get(number);
        image.setImageResource(imageData.getImage());
        GV_adapter adapter = new GV_adapter(this, consume);
        GV.setAdapter(adapter);
        GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = position;
                GV_data imageData = consume.get(number);
                image.setImageResource(imageData.getImage());
            }
        });
        calculator(10, 2);
    }

    private void calculator(int integral, int decimal) {
        //对接计算器中的控件
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
                    if (num.length() == 0) {
                        money.setText("00.0");
                    }
                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加预算记录
                if (num.length() > 0 && Double.valueOf(num) >= 0.01) {
                    String years  = new SimpleDateFormat("yyyy").format(System.currentTimeMillis());
                    String months = new SimpleDateFormat("MM").format(System.currentTimeMillis());
                    //从原有记录中查找是否存在同年同月同用户同类型的记录
                    List<Bill_budget_data> budgets = LitePal.
                            where("year=? and month=? and category=? and u_id=?",
                                    years,
                                    String.valueOf(Short.valueOf(months)),
                                    consume.get(number).getDescribe(),
                                    String.valueOf(User.getUserId())).
                            find(Bill_budget_data.class);
                    Bill_budget_data data;
                    if (budgets.isEmpty()) {
                        //如果不存在则新增一条记录
                        data = new Bill_budget_data();
                        data.setYear(Short.valueOf(years));
                        data.setMonth(Short.valueOf(months));
                        data.setCategory(consume.get(number).getDescribe());
                        data.setU_id(User.getUserId());
                        data.setPlan(Double.valueOf(num));
                        data.setSpending(Double.valueOf(0));
                        data.setRemark("remark");
                    } else {
                        //如果存在则修改原记录
                        data = budgets.get(0);
                        data.setPlan(data.getPlan() + Double.valueOf(num));
                    }
                    //保存并关闭该界面
                    data.save();
                    finish();
                }
            }
        });
    }
}