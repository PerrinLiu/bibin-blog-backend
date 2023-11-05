//package com.llpy.userservice.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class SpringSecurityConfigurer extends WebSecurityConfigurerAdapter {
//
//    //重写configure(HttpSecurity http)方法
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()  // 关闭csrf
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)     // 指定session的创建策略，不使用session
//                .and()     // 再次获取到HttpSecurity对象
//                .authorizeRequests()   // 进行认证请求的配置
//                .antMatchers("/user/login").anonymous()   // 对于登录接口，允许匿名访问
//                .antMatchers("/user/addUser").hasAuthority("system:user:add")
//                .anyRequest().authenticated();  // 除了上面的请求以外所有的请求全部需要认证
//
//        //配置跨域
//        http.cors();
//    }
//
//
//}