package hlccd.regular.My;

/**
 * 月度报表收支数据类型
 * 包含两个属性:
 * 该项记录的名称
 * 该项记录的金额
 *
 * 用于建立List传入适配器中展示登录用户的某月收支报表
 * List中第一个数据为当月总数据
 * 后续数据为按照收支数据大小的降序排列
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */
public class My_statement_data implements Comparable<My_statement_data> {
    private String name;
    private double cheek;

    My_statement_data(String name, double cheek) {
        this.name  = name;
        this.cheek = cheek;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCheek() {
        return cheek;
    }

    public void setCheek(double cheek) {
        this.cheek = cheek;
    }

    /**
     * 比较器
     * 用于在排序时使得数据按照金额大小降序排序
     */
    @Override
    public int compareTo(My_statement_data o) {
        if (this.cheek > o.cheek) {
            return -1;
        } else if (this.cheek < o.cheek) {
            return 1;
        }
        return 0;
    }
}
