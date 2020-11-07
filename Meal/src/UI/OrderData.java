package UI;

import java.time.LocalDateTime;

/**
 * DB에서 조회한 orderlist 데이터를 저장하기 위한 클래스
 */
public class OrderData {
    private int orderNo;
    private int cuisineNo;
    private int mealNo;
    private int memberNo;
    private int orderCount;
    private int amount;
    private String orderDate;

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getCuisineNo() {
        return cuisineNo;
    }

    public void setCuisineNo(int cuisineNo) {
        this.cuisineNo = cuisineNo;
    }

    public int getMealNo() {
        return mealNo;
    }

    public void setMealNo(int mealNo) {
        this.mealNo = mealNo;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Object[] getObjects() {
        Object[] objects = new Object[]{orderNo, cuisineNo, mealNo, memberNo, orderCount, amount, orderDate};
        return objects;
    }
}
