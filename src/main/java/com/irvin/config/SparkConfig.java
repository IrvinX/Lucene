//package com.irvin.config;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//import org.springframework.core.env.Environment;
//
//import java.io.Serializable;
//
///**
// * @author irvin
// * @date Create in 上午2:52 2017/8/5
// * @description
// */
//@Configuration
//@PropertySource("classpath:application.properties")
//public class SparkConfig implements Serializable {
//
//    /**
//     *
//     */
//    private static final long serialVersionUID = -5017266850264030516L;
//
//    @Autowired
//    private Environment env;
//
//    @Value("${app.name}")
//    private String appName;
//
//    @Value("${spark.home}")
//    private String sparkHome;
//
//    @Value("${master.uri}")
//    private String masterUri;
//
//    @Bean
//    public SparkConf sparkConf() {
//        SparkConf sparkConf = new SparkConf().setAppName(appName)
//                // .setSparkHome(sparkHome)
//                .setMaster(masterUri);
//
//        return sparkConf;
//    }
//
//    @Bean
//    public JavaSparkContext javaSparkContext() {
//        return new JavaSparkContext(sparkConf());
//    }
//
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//}
