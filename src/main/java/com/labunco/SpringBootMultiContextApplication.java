package com.labunco;

import com.labunco.configuration.BaseConfiguration;
import com.labunco.configuration.FirstSecurityConfiguration;
import com.labunco.configuration.SecondSecurityConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootConfiguration
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, DispatcherServletAutoConfiguration.class})
@Import({
        BaseConfiguration.class,
        FirstSecurityConfiguration.class,
        SecondSecurityConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class})
public class SpringBootMultiContextApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMultiContextApplication.class, args);
    }

    

    @Bean
    public ServletRegistrationBean firstApi(ApplicationContext parent) {
        return createChildContextServlet(parent, "/first/*", "first");
    }

    @Bean
    public ServletRegistrationBean secondApi(ApplicationContext parent) {
        return createChildContextServlet(parent, "/second/*", "second");
    }

    private ServletRegistrationBean createChildContextServlet(ApplicationContext parent, String path, String name) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setParent(parent);
        ctx.register(PropertyPlaceholderAutoConfiguration.class,
                DispatcherServletAutoConfiguration.class);
        dispatcherServlet.setApplicationContext(ctx);

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, path);
        servletRegistrationBean.setName(name);

        return servletRegistrationBean;
    }
}
