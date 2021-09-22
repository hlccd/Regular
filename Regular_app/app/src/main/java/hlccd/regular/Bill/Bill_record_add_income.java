package hlccd.regular.Bill;
/**
 * 记账收入记录子界面
 * 该界面包括收入记录相关的二级类型,点击即可进行选择
 * 上方的符号区和记录类型组中选中项的图片进行绑定
 * 同时上方金额栏和下方计算器键盘进行绑定
 * 金额限定为10位整数2位小数
 *
 * @author hlccd 2020.6.7
 * @version 1.0
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import hlccd.regular.util.GV_adapter;
import hlccd.regular.util.GV_data;
import hlccd.regular.R;
import hlccd.regular.User.User;

public class Bill_record_add_income extends Fragment implements RadioGroup.OnCheckedChangeListener {
    private ImageView     image;
    private GridView      GV;
    private List<GV_data> income = new GV_data().ToGV(new Bill_type_second_data().income());
    private TextView      money;
    private View          view;
    private int           number = 0;
    //计算器中的控件
    private String        num    = "";
    private int           digit  = -1;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        image     = view.findViewById(R.id.bill_record_add_income_image);
        GV        = view.findViewById(R.id.bill_record_add_income_GV);
        money     = view.findViewById(R.id.bill_record_add_income_money);
        GV_data imageData = income.get(number);
        image.setImageResource(imageData.getImage());
        GV_adapter adapter = new GV_adapter(this.getContext(), income);
        GV.setAdapter(adapter);
        GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = position;
                GV_data imageData = income.get(number);
                image.setImageResource(imageData.getImage());
            }
        });
        calculator(10, 2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_record_add_income, container, false);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    private void calculator(int integral, int decimal) {
        //对接计算器中的控件
        one      = view.findViewById(R.id.one);
        two      = view.findViewById(R.id.two);
        three    = view.findViewById(R.id.three);
        four     = view.findViewById(R.id.four);
        five     = view.findViewById(R.id.five);
        six      = view.findViewById(R.id.six);
        seven    = view.findViewById(R.id.seven);
        eight    = view.findViewById(R.id.eight);
        nine     = view.findViewById(R.id.nine);
        AC       = view.findViewById(R.id.AC);
        zero     = view.findViewById(R.id.zero);
        dot      = view.findViewById(R.id.dot);
        delete   = view.findViewById(R.id.delete);
        rollback = view.findViewById(R.id.rollback);
        finish   = view.findViewById(R.id.finish);
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
                getActivity().finish();
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
                    Bill_record_data record = new Bill_record_data();
                    record.setU_id(User.getUserId());
                    record.setMoney(Double.valueOf(num));
                    record.setCategory(income.get(number).getDescribe());
                    record.setType("收入");
                    record.setRemark("remark");
                    record.setTimestamp(System.currentTimeMillis());
                    record.save();
                    getActivity().finish();
                }
            }
        });
    }
}