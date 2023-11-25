package com.posadskiy.spring.di;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;

@Log4j2
public class DefaultInitBean implements InitializingBean {

    public void afterPropertiesSet() throws Exception {
        log.info("DEFAULT INIT BEAN");
    }
}
