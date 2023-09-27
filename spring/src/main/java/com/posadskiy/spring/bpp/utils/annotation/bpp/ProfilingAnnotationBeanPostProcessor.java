package com.posadskiy.spring.bpp.utils.annotation.bpp;

import com.posadskiy.spring.bpp.utils.annotation.Profiling;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProfilingAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, Class> beans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // TODO: Попробовать методы beanClass
        if (beanClass.isAnnotationPresent(Profiling.class)) {
            beans.put(beanName, beanClass);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class annotatedBean = beans.get(beanName);
        if (annotatedBean != null) {
            return Proxy.newProxyInstance(annotatedBean.getClassLoader(), annotatedBean.getInterfaces(), (proxy, method, args) -> {
                Long start = System.nanoTime();
                Object retVal = method.invoke(bean, args);
                Long finish = System.nanoTime();
                System.out.println("TIME: ".concat(String.valueOf(finish - start)));
                return retVal;
            });
        }
        return bean;
    }
}
