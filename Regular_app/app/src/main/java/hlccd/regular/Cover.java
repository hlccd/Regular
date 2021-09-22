package hlccd.regular;
/**
 * 软件封面
 * 封面将会在软件启动时进行展示
 * 展示1s后将跳转到主页,同时销毁封面activity
 * 跳转前进行账单一二级类型初始化,避免后续使用中出现无可选类型
 *
 * @author hlccd 2020.6.5
 * @version 1.0
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hlccd.regular.API.IM;
import hlccd.regular.Bill.Bill_type_root_data;
import hlccd.regular.Bill.Bill_type_second_data;

public class Cover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);

        LitePal.getDatabase();
        initTypeRoot();
        initTypeSecond();
        /*
            以下为延时跳转部分,延时1s后进行活动跳转并销毁封面活动
         */
        Intent intent = new Intent(Cover.this, Homepage.class);
        Timer  timer  = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task, 1 * 1000);
    }

    //账单一级类型初始化
    private void initTypeRoot() {
        new Bill_type_root_data("餐饮消费", "消费").save();
        new Bill_type_root_data("购物消费", "消费").save();
        new Bill_type_root_data("交通消费", "消费").save();
        new Bill_type_root_data("家用消费", "消费").save();
        new Bill_type_root_data("娱乐消费", "消费").save();
        new Bill_type_root_data("医疗消费", "消费").save();
        new Bill_type_root_data("教育消费", "消费").save();
        new Bill_type_root_data("人情消费", "消费").save();
        new Bill_type_root_data("经营消费", "消费").save();
        new Bill_type_root_data("其他消费", "消费").save();

        new Bill_type_root_data("职业收入", "收入").save();
        new Bill_type_root_data("生意收入", "收入").save();
        new Bill_type_root_data("人情收入", "收入").save();
        new Bill_type_root_data("其他收入", "收入").save();
    }

    //账单二级类型初始化
    private void initTypeSecond() {
        List<Bill_type_root_data> roots = LitePal.findAll(Bill_type_root_data.class);
        for (Bill_type_root_data root : roots) {
            if (root.getType().equals("消费")) {
                switch (root.getName()) {
                    case "餐饮消费":
                        new Bill_type_second_data("餐饮", root.getName(), root.getType(), R.drawable.bill_consume_catering_catering).save();
                        new Bill_type_second_data("早餐", root.getName(), root.getType(), R.drawable.bill_consume_catering_breakfast).save();
                        new Bill_type_second_data("午餐", root.getName(), root.getType(), R.drawable.bill_consume_catering_lunch).save();
                        new Bill_type_second_data("晚餐", root.getName(), root.getType(), R.drawable.bill_consume_catering_dinner).save();
                        new Bill_type_second_data("零食", root.getName(), root.getType(), R.drawable.bill_consume_catering_snack).save();
                        new Bill_type_second_data("水果", root.getName(), root.getType(), R.drawable.bill_consume_catering_fruit).save();
                        new Bill_type_second_data("蔬菜", root.getName(), root.getType(), R.drawable.bill_consume_catering_vegetables).save();
                        new Bill_type_second_data("饮料", root.getName(), root.getType(), R.drawable.bill_consume_catering_drinks).save();
                        break;
                    case "购物消费":
                        new Bill_type_second_data("购物", root.getName(), root.getType(), R.drawable.bill_custome_shopping_shopping).save();
                        new Bill_type_second_data("日用品", root.getName(), root.getType(), R.drawable.bill_custome_shopping_commodity).save();
                        new Bill_type_second_data("服饰", root.getName(), root.getType(), R.drawable.bill_custome_shopping_dress).save();
                        new Bill_type_second_data("鞋帽", root.getName(), root.getType(), R.drawable.bill_consume_shopping_shoes).save();
                        new Bill_type_second_data("化妆品", root.getName(), root.getType(), R.drawable.bill_consume_shopping_cosmetics).save();
                        new Bill_type_second_data("数码", root.getName(), root.getType(), R.drawable.bill_consume_shopping_digital).save();
                        new Bill_type_second_data("家居", root.getName(), root.getType(), R.drawable.bill_consume_shopping_home).save();
                        new Bill_type_second_data("电器", root.getName(), root.getType(), R.drawable.bill_consume_shopping_domestic).save();
                        break;
                    case "交通消费":
                        new Bill_type_second_data("交通", root.getName(), root.getType(), R.drawable.bill_consume_traffic_traffic).save();
                        new Bill_type_second_data("油费", root.getName(), root.getType(), R.drawable.bill_consume_traffic_oil).save();
                        new Bill_type_second_data("停车费", root.getName(), root.getType(), R.drawable.bill_consume_traffic_parking).save();
                        new Bill_type_second_data("车险", root.getName(), root.getType(), R.drawable.bill_consume_traffic_auto_insurance).save();
                        new Bill_type_second_data("过路费", root.getName(), root.getType(), R.drawable.bill_consume_traffic_toll).save();
                        new Bill_type_second_data("车检", root.getName(), root.getType(), R.drawable.bill_consume_traffic_inspection).save();
                        new Bill_type_second_data("单车", root.getName(), root.getType(), R.drawable.bill_consume_traffic_bicycle).save();
                        new Bill_type_second_data("公交", root.getName(), root.getType(), R.drawable.bill_consume_traffic_bus).save();
                        new Bill_type_second_data("地铁", root.getName(), root.getType(), R.drawable.bill_consume_traffic_subway).save();
                        new Bill_type_second_data("打车", root.getName(), root.getType(), R.drawable.bill_consume_traffic_taxi).save();
                        new Bill_type_second_data("火车", root.getName(), root.getType(), R.drawable.bill_consume_traffic_train).save();
                        new Bill_type_second_data("高铁", root.getName(), root.getType(), R.drawable.bill_consume_traffic_railway).save();
                        new Bill_type_second_data("游船", root.getName(), root.getType(), R.drawable.bill_consume_traffic_boat).save();
                        new Bill_type_second_data("飞机", root.getName(), root.getType(), R.drawable.bill_consume_traffic_plane).save();
                        new Bill_type_second_data("违章处罚", root.getName(), root.getType(), R.drawable.bill_consume_traffic_punish).save();
                        break;
                    case "家用消费":
                        new Bill_type_second_data("家用", root.getName(), root.getType(), R.drawable.bill_consume_family_family).save();
                        new Bill_type_second_data("房租", root.getName(), root.getType(), R.drawable.bill_consume_family_house).save();
                        new Bill_type_second_data("水费", root.getName(), root.getType(), R.drawable.bill_consume_family_water).save();
                        new Bill_type_second_data("电费", root.getName(), root.getType(), R.drawable.bill_consume_family_electric).save();
                        new Bill_type_second_data("煤气费", root.getName(), root.getType(), R.drawable.bill_consume_family_gas).save();
                        new Bill_type_second_data("话费", root.getName(), root.getType(), R.drawable.bill_consume_family_telephone).save();
                        new Bill_type_second_data("网费", root.getName(), root.getType(), R.drawable.bill_consume_family_net).save();
                        new Bill_type_second_data("修缮清洁", root.getName(), root.getType(), R.drawable.bill_consume_family_clear).save();
                        new Bill_type_second_data("保姆", root.getName(), root.getType(), R.drawable.bill_consume_family_housemaid).save();
                        break;
                    case "娱乐消费":
                        new Bill_type_second_data("娱乐", root.getName(), root.getType(), R.drawable.bill_consume_amusement_amusement).save();
                        new Bill_type_second_data("会员费", root.getName(), root.getType(), R.drawable.bill_consume_amusement_membership).save();
                        new Bill_type_second_data("娱乐设备", root.getName(), root.getType(), R.drawable.bill_consume_amusement_equipment).save();
                        new Bill_type_second_data("电影", root.getName(), root.getType(), R.drawable.bill_consume_amusement_film).save();
                        new Bill_type_second_data("美容美发", root.getName(), root.getType(), R.drawable.bill_consume_amusement_hairdressing).save();
                        new Bill_type_second_data("运动健身", root.getName(), root.getType(), R.drawable.bill_consume_amusement_exercise).save();
                        new Bill_type_second_data("游戏", root.getName(), root.getType(), R.drawable.bill_consume_amusement_game).save();
                        new Bill_type_second_data("摄影", root.getName(), root.getType(), R.drawable.bill_consume_amusement_photograph).save();
                        new Bill_type_second_data("歌舞", root.getName(), root.getType(), R.drawable.bill_consume_amusement_dance).save();
                        new Bill_type_second_data("旅游", root.getName(), root.getType(), R.drawable.bill_consume_amusement_travel).save();
                        new Bill_type_second_data("住宿", root.getName(), root.getType(), R.drawable.bill_consume_amusement_stay).save();
                        new Bill_type_second_data("门票", root.getName(), root.getType(), R.drawable.bill_consume_amusement_tickets).save();
                        new Bill_type_second_data("参团", root.getName(), root.getType(), R.drawable.bill_consume_amusement_participation).save();
                        new Bill_type_second_data("导游", root.getName(), root.getType(), R.drawable.bill_consume_amusement_guide).save();
                        new Bill_type_second_data("特产纪念品", root.getName(), root.getType(), R.drawable.bill_consume_amusement_specialty).save();
                        break;
                    case "医疗消费":
                        new Bill_type_second_data("医疗", root.getName(), root.getType(), R.drawable.bill_consume_medical_medical).save();
                        new Bill_type_second_data("药物", root.getName(), root.getType(), R.drawable.bill_consume_medical_medicines).save();
                        new Bill_type_second_data("手术", root.getName(), root.getType(), R.drawable.bill_consume_medical_operation).save();
                        new Bill_type_second_data("看护", root.getName(), root.getType(), R.drawable.bill_consume_medical_nurse).save();
                        new Bill_type_second_data("保险", root.getName(), root.getType(), R.drawable.bill_consume_medical_insurance).save();
                        new Bill_type_second_data("诊疗", root.getName(), root.getType(), R.drawable.bill_consume_medical_treatment).save();
                        break;
                    case "教育消费":
                        new Bill_type_second_data("教育", root.getName(), root.getType(), R.drawable.bill_consume_education_education).save();
                        new Bill_type_second_data("学费", root.getName(), root.getType(), R.drawable.bill_consume_education_tuition).save();
                        new Bill_type_second_data("文具", root.getName(), root.getType(), R.drawable.bill_consume_education_stationery).save();
                        new Bill_type_second_data("图书", root.getName(), root.getType(), R.drawable.bill_consume_education_books).save();
                        new Bill_type_second_data("培训", root.getName(), root.getType(), R.drawable.bill_consume_education_training).save();
                        new Bill_type_second_data("考试", root.getName(), root.getType(), R.drawable.bill_consume_education_examination).save();
                        break;
                    case "人情消费":
                        new Bill_type_second_data("人情", root.getName(), root.getType(), R.drawable.bill_consume_concern_concern).save();
                        new Bill_type_second_data("请客", root.getName(), root.getType(), R.drawable.bill_consume_concern_fete).save();
                        new Bill_type_second_data("礼物", root.getName(), root.getType(), R.drawable.bill_consume_concern_gift).save();
                        new Bill_type_second_data("红包", root.getName(), root.getType(), R.drawable.bill_consume_concern_packet).save();
                        new Bill_type_second_data("礼金", root.getName(), root.getType(), R.drawable.bill_consume_concern_cash).save();
                        new Bill_type_second_data("借出", root.getName(), root.getType(), R.drawable.bill_consume_concern_lend).save();
                        break;
                    case "经营消费":
                        new Bill_type_second_data("经营", root.getName(), root.getType(), R.drawable.bill_consume_operate_operate).save();
                        new Bill_type_second_data("税款", root.getName(), root.getType(), R.drawable.bill_consume_operate_tax).save();
                        new Bill_type_second_data("货物材料", root.getName(), root.getType(), R.drawable.bill_consume_operate_goods).save();
                        new Bill_type_second_data("生产设备", root.getName(), root.getType(), R.drawable.bill_consume_operate_equipment).save();
                        new Bill_type_second_data("房租物业", root.getName(), root.getType(), R.drawable.bill_consume_operate_property).save();
                        new Bill_type_second_data("差旅费", root.getName(), root.getType(), R.drawable.bill_consume_operate_travel).save();
                        new Bill_type_second_data("运输费", root.getName(), root.getType(), R.drawable.bill_consume_operate_transportation).save();
                        new Bill_type_second_data("人工工资", root.getName(), root.getType(), R.drawable.bill_consume_operate_labor).save();
                        new Bill_type_second_data("人力培训", root.getName(), root.getType(), R.drawable.bill_consume_operate_training).save();
                        new Bill_type_second_data("团队建设", root.getName(), root.getType(), R.drawable.bill_consume_operate_team).save();
                        new Bill_type_second_data("营销推广", root.getName(), root.getType(), R.drawable.bill_consume_operate_marketing).save();
                        new Bill_type_second_data("客户招待", root.getName(), root.getType(), R.drawable.bill_consume_operate_customer).save();
                        new Bill_type_second_data("登记注册", root.getName(), root.getType(), R.drawable.bill_consume_operate_registration).save();
                        new Bill_type_second_data("坏账", root.getName(), root.getType(), R.drawable.bill_consume_operate_bad_bedt).save();
                        break;
                    case "其他消费":
                        new Bill_type_second_data("其他", root.getName(), root.getType(), R.drawable.bill_consume_other_other).save();
                        new Bill_type_second_data("租赁费", root.getName(), root.getType(), R.drawable.bill_consume_other_rental).save();
                        new Bill_type_second_data("签证", root.getName(), root.getType(), R.drawable.bill_consume_other_visa).save();
                        new Bill_type_second_data("小费", root.getName(), root.getType(), R.drawable.bill_consume_other_tip).save();
                        new Bill_type_second_data("偿还贷款", root.getName(), root.getType(), R.drawable.bill_consume_other_repay).save();
                        new Bill_type_second_data("投资亏损", root.getName(), root.getType(), R.drawable.bill_consume_other_investment).save();
                        new Bill_type_second_data("意外支出", root.getName(), root.getType(), R.drawable.bill_consume_other_unexpected).save();
                        new Bill_type_second_data("宠物", root.getName(), root.getType(), R.drawable.bill_consume_other_pet).save();
                        break;
                }
            } else if (root.getType().equals("收入")) {
                switch (root.getName()) {
                    case "职业收入":
                        new Bill_type_second_data("职业收入", root.getName(), root.getType(), R.drawable.bill_income_occupational_occupational).save();
                        new Bill_type_second_data("兼职", root.getName(), root.getType(), R.drawable.bill_income_occupational_part_time).save();
                        new Bill_type_second_data("工资", root.getName(), root.getType(), R.drawable.bill_income_occupational_salary).save();
                        new Bill_type_second_data("奖金", root.getName(), root.getType(), R.drawable.bill_income_occupational_prize).save();
                        new Bill_type_second_data("福利", root.getName(), root.getType(), R.drawable.bill_income_occupational_welfare).save();
                        new Bill_type_second_data("提成", root.getName(), root.getType(), R.drawable.bill_income_occupational_commission).save();
                        new Bill_type_second_data("分红", root.getName(), root.getType(), R.drawable.bill_income_occupational_distribute).save();
                        new Bill_type_second_data("社保金", root.getName(), root.getType(), R.drawable.bill_income_occupational_social_security).save();
                        new Bill_type_second_data("公积金", root.getName(), root.getType(), R.drawable.bill_income_occupational_public_accumulation).save();
                        new Bill_type_second_data("报销", root.getName(), root.getType(), R.drawable.bill_income_occupational_reimburse).save();
                        break;
                    case "生意收入":
                        new Bill_type_second_data("生意收入", root.getName(), root.getType(), R.drawable.bill_income_business_business).save();
                        new Bill_type_second_data("销售额", root.getName(), root.getType(), R.drawable.bill_income_business_sales).save();
                        new Bill_type_second_data("收款", root.getName(), root.getType(), R.drawable.bill_income_business_collect).save();
                        new Bill_type_second_data("退款", root.getName(), root.getType(), R.drawable.bill_income_business_redund).save();
                        new Bill_type_second_data("贷款", root.getName(), root.getType(), R.drawable.bill_income_business_loan).save();
                        new Bill_type_second_data("退税", root.getName(), root.getType(), R.drawable.bill_income_business_redund_tax).save();
                        new Bill_type_second_data("融资", root.getName(), root.getType(), R.drawable.bill_income_business_finance).save();
                        new Bill_type_second_data("赞助费", root.getName(), root.getType(), R.drawable.bill_income_business_sponsorship).save();
                        break;
                    case "人情收入":
                        new Bill_type_second_data("人情收入", root.getName(), root.getType(), R.drawable.bill_income_concern_concern).save();
                        new Bill_type_second_data("生活费", root.getName(), root.getType(), R.drawable.bill_income_concern_living).save();
                        new Bill_type_second_data("零花钱", root.getName(), root.getType(), R.drawable.bill_income_concern_pin_money).save();
                        new Bill_type_second_data("收红包", root.getName(), root.getType(), R.drawable.bill_income_concern_packet).save();
                        new Bill_type_second_data("收礼金", root.getName(), root.getType(), R.drawable.bill_income_concern_gifts).save();
                        new Bill_type_second_data("压岁钱", root.getName(), root.getType(), R.drawable.bill_income_concern_lucky_money).save();
                        break;
                    case "其他收入":
                        new Bill_type_second_data("其他收入", root.getName(), root.getType(), R.drawable.bill_income_other_other).save();
                        new Bill_type_second_data("投资收益", root.getName(), root.getType(), R.drawable.bill_income_other_yield).save();
                        new Bill_type_second_data("利息", root.getName(), root.getType(), R.drawable.bill_income_other_interest).save();
                        new Bill_type_second_data("借入款", root.getName(), root.getType(), R.drawable.bill_income_other_borrowing).save();
                        new Bill_type_second_data("奖学金", root.getName(), root.getType(), R.drawable.bill_income_other_scholarship).save();
                        new Bill_type_second_data("租金", root.getName(), root.getType(), R.drawable.bill_income_other_rent).save();
                        break;
                }
            }
        }
    }
}