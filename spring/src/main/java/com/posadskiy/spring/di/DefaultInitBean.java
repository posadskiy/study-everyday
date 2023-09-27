package com.posadskiy.spring.di;

import org.springframework.beans.factory.InitializingBean;

public class DefaultInitBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        System.out.println("DEFAULT INIT BEAN");
    }
}
