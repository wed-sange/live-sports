package com.edi.sdk.football.domain;

import lombok.Data;

import java.util.List;

/**
 * null
 */
@Data
public class OddsBfHistory {
    /**
     * 成交统计
     */
    private Amount amount;

    @Data
    public static class Amount {
        /**
         * 客
         */
        private List<Away> away;

        @Data
        public static class Away {
            /**
             * 成交量
             */
            private Integer amount;
            /**
             * 价位
             */
            private Float price;
            /**
             * 变化时间
             */
            private Integer time;
            /**
             * 类型，1-买、2-卖、0-非买卖
             */
            private Integer type;
        }

        /**
         * 平
         */
        private List<Draw> draw;

        @Data
        public static class Draw {
            /**
             * 成交量
             */
            private Integer amount;
            /**
             * 价位
             */
            private Float price;
            /**
             * 变化时间
             */
            private Integer time;
            /**
             * 类型，1-买、2-卖、0-非买卖
             */
            private Integer type;
        }

        /**
         * 主
         */
        private List<Home> home;

        @Data
        public static class Home {
            /**
             * 成交量
             */
            private Integer amount;
            /**
             * 价位
             */
            private Float price;
            /**
             * 变化时间
             */
            private Integer time;
            /**
             * 类型，1-买、2-卖、0-非买卖
             */
            private Integer type;
        }
    }

    /**
     * 平均欧赔
     */
    private List<Euroavg> euroavg;

    @Data
    public static class Euroavg {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }

    /**
     * 实时价位
     */
    private List<Odds> odds;

    @Data
    public static class Odds {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }

    /**
     * 盈亏指数
     */
    private List<Payout> payout;

    @Data
    public static class Payout {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }

    /**
     * 交易指数
     */
    private List<Index> index;

    @Data
    public static class Index {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }

    /**
     * 成交比例(%)
     */
    private List<Per> per;

    @Data
    public static class Per {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }

    /**
     * 冷热指数
     */
    private List<Hot> hot;

    @Data
    public static class Hot {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }

    /**
     * 挂牌比例(%)
     */
    private List<Stock> stock;

    @Data
    public static class Stock {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }

    /**
     * 凯利指数
     */
    private List<Kelly> kelly;

    @Data
    public static class Kelly {
        /**
         * 客
         */
        private Float away;
        /**
         * 变化时间
         */
        private Integer time;
        /**
         * 平
         */
        private Float draw;
        /**
         * 主
         */
        private Float home;
    }
}