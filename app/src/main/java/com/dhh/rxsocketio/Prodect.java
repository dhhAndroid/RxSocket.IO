package com.dhh.rxsocketio;

/**
 * Created by dhh on 2017/11/14.
 */

public class Prodect {

    /**
     * id : GS-SR-0001-0002-0000-0001
     * robotInfo : {"model_type":"GS-SR-TD-CIRCLE","product_id":"GS-SR-0001-0002-0000-0001"}
     * client : false
     */

    public String id;
    public RobotInfoBean robotInfo;
    public boolean client;

    public static class RobotInfoBean {
        /**
         * model_type : GS-SR-TD-CIRCLE
         * product_id : GS-SR-0001-0002-0000-0001
         */

        public String model_type;
        public String product_id;

        public RobotInfoBean(String model_type, String product_id) {
            this.model_type = model_type;
            this.product_id = product_id;
        }

        @Override
        public String toString() {
            return "RobotInfoBean{" +
                    "model_type='" + model_type + '\'' +
                    ", product_id='" + product_id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Prodect{" +
                "id='" + id + '\'' +
                ", robotInfo=" + robotInfo +
                ", client=" + client +
                '}';
    }
}
