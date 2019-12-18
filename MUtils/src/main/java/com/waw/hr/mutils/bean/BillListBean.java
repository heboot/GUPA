package com.waw.hr.mutils.bean;

import com.waw.hr.mutils.base.BaseBeanEntity;

import java.util.List;

public class BillListBean extends BaseBeanEntity {


    /**
     * total_money : 3000
     * list : [{"id":1,"number":1,"money":"500.00","status":2,"refund_time":"2019-07-13 15:53:56"},{"id":2,"number":2,"money":"500.00","status":2,"refund_time":"2019-08-13 15:53:56"},{"id":3,"number":3,"money":"500.00","status":2,"refund_time":"2019-09-13 15:53:56"},{"id":4,"number":4,"money":"500.00","status":2,"refund_time":"2019-10-13 15:53:56"},{"id":5,"number":5,"money":"500.00","status":2,"refund_time":"2019-11-13 15:53:56"},{"id":6,"number":6,"money":"500.00","status":2,"refund_time":"2019-12-13 15:53:56"}]
     */

    private int total_money;
    private List<BillModel> list;

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public List<BillModel> getList() {
        return list;
    }

    public void setList(List<BillModel> list) {
        this.list = list;
    }

    public static class BillModel {
        /**
         * id : 1
         * number : 1
         * money : 500.00
         * status : 2
         * refund_time : 2019-07-13 15:53:56
         */

        private int id;
        private int number;
        private String money;
        private int status;
        private String refund_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getRefund_time() {
            return refund_time;
        }

        public void setRefund_time(String refund_time) {
            this.refund_time = refund_time;
        }
    }
}
