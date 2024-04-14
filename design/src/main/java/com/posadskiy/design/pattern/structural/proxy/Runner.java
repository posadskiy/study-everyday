package com.posadskiy.design.pattern.structural.proxy;

import java.util.logging.Logger;

public class Runner {
    public static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) {
        var simpleGuy = new SimpleGuy();
        var proxyGuy = new ProxyGuy(simpleGuy);

        logger.info(String.valueOf(proxyGuy.spend(100)));
        logger.info(String.valueOf(proxyGuy.earn(200)));
        logger.info(String.valueOf(proxyGuy.spend(100)));
    }
}
