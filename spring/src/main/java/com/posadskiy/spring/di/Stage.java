package com.posadskiy.spring.di;

public class Stage {

    private Stage() {
    }

    public static Stage getInstance() {
        return StageSingletonHolder.instance;
    }

    private static class StageSingletonHolder {
        static Stage instance = new Stage();
    }

}
