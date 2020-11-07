package UI;

import DB.Driver_Connect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TableDialog extends JDialog {
    private DefaultTableModel defaultTableModel;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private Connection con = Driver_Connect.makeConnection("/meal");
    private DateFormat dateFormat;

    public TableDialog(Frame jframe, int month, int isDinner) {
        super(jframe, "점심, 저녁별 조회");
        defaultTableModel = new DefaultTableModel(
                new String[]{"orderNo", "cuisineNo", "mealNo", "memberNo", "orderCount", "amount", "orderDate"}
                , 0); //테이블모델에 컬럼 추가

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<OrderData> orderList = searchData(month, isDinner);
        for (OrderData o : orderList) {
            defaultTableModel.addRow(o.getObjects());
        }


        jTable = new JTable(defaultTableModel); //jTable(뷰)에 데이터 추가
        jScrollPane = new JScrollPane(jTable);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(jScrollPane);
        this.pack();
        this.setVisible(true);
    }

    /**
     *
     * @param month
     * @param isDinner 0 = 점심, 1 = 저녁
     */
    public List<OrderData> searchData(int month, int isDinner) {
        List orderList = new ArrayList<OrderData>();

        try {
            PreparedStatement ps = con.prepareStatement("select *, date_format(orderdate,'%H') from orderlist where MONTH(orderDate)=" + month);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderData orderData = new OrderData();
                orderData.setOrderNo(rs.getInt(1));
                orderData.setCuisineNo(rs.getInt(2));
                orderData.setMealNo(rs.getInt(3));
                orderData.setMemberNo(rs.getInt(4));
                orderData.setOrderCount(rs.getInt(5));
                orderData.setAmount(rs.getInt(6));
                orderData.setOrderDate(rs.getTimestamp(7).toLocalDateTime().minusHours(9).toString());
                int orderTime = rs.getInt(8);

                if (isDinner == 1 && orderTime >= 17 && orderTime <= 20) { //저녁
                    orderList.add(orderData);
                } else if (isDinner == 0 && orderTime >= 11 && orderTime < 14) { //점심
                    orderList.add(orderData);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return orderList;
    }
}
