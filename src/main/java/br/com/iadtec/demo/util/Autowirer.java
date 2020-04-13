package br.com.iadtec.demo.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Autowirer {

    private static ApplicationContext applicationContext;

    private Autowirer(ApplicationContext applicationContext) {
        Autowirer.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> bean) {
        return applicationContext.getBean(bean);
    }
}
