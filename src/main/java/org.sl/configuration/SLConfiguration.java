package org.sl.configuration;

import org.sl.util.RedisAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 商城项目所用到的所有配置类
 *
 * @ClassName SLConfiguration
 * @Author tianye
 * @Date 2019/3/25 0:53
 * @Version 1.0
 */
@Configuration
public class SLConfiguration {

    //配置文件上传:CommonsMultipartResolver
//    @Bean(name = "multipartResolver")
//    public MultipartResolver multipartResolver(){
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("UTF-8");
//        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
//        resolver.setMaxInMemorySize(40960);
//        resolver.setMaxUploadSize(50*1024*1024);//上传文件大小 50M 50*1024*1024
//        return resolver;
//    }

    @Bean(name = "redisAPI")
    public RedisAPI getRedis(){
        return new RedisAPI();
    }

}
