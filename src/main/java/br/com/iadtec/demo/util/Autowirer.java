package br.com.iadtec.demo.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Classe utilitária responsável por fornecer instâncias de Classes gerenciadas pelo Spring(Beans).
 * @see ApplicationContext#getBean(Class)
 */
@Service
public class Autowirer implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> bean) {
        return applicationContext.getBean(bean);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Autowirer.applicationContext = applicationContext;
    }
}
