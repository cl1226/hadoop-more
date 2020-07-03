package com.cnnc.beanpostprocesser;

import com.cnnc.beanpostprocesser.annotation.RountingInjected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Test {

    @RountingInjected(value = "helloServiceImpl2")
//    @Autowired
//    @Qualifier(value = "helloServiceImpl2")
    private HelloService helloService;

    public void testSayHello() {
        helloService.sayHello();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.cnnc");
//        Test test = applicationContext.getBean(Test.class);
//        test.testSayHello();

        Car bean = (Car) applicationContext.getBean("car");
        System.out.println(bean);

        Object bean1 = applicationContext.getBean("&car");
        System.out.println(bean1);

        int a = 8;
        a = a++;
        System.out.println(a);
    }
}
