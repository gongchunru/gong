package com.gcr.rpc.client;

import java.math.BigDecimal;

/**
 * Created by gongchunru
 * Date：2017/8/29.
 * Time：10:38
 */
public class Test {

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("40000.1234");

        a.intValue();
        System.out.println((a.multiply(new BigDecimal(100))).intValue());
    }
}
