package com.cnnc;

import com.cnnc.spi.IService;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletContainerInitializer;
import java.util.ServiceLoader;

public class Application {

    public static void main(String[] args) throws LifecycleException {
//        run();

        {
            int a = 1;
        }
        {
            int b = 1;
        }
        int b = 2;
//        System.out.println(a);


//        ServiceLoader<IService> serviceLoader  = ServiceLoader.load(IService.class);
//        for(IService service : serviceLoader) {
//            service.say();
//        }

    }

    public static void run() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        System.out.println("=============" + Application.class.getResource("/").getPath());

        Context context = tomcat.addContext("/", Application.class.getResource("/").getPath());
//        Context context = tomcat.addWebapp("/", "webapp");

//        context.addServletMapping();
//        tomcat.addServlet("/user.do", );

//        context.addServletContainerInitializer();

        tomcat.start();
        tomcat.getServer().await();
    }

}
