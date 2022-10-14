package com.couponsystemwithjwt.config;

import com.couponsystemwithjwt.state.MySession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class LoginConfig {

    @Bean
    public HashMap<Long, MySession> sessions(){
        return new HashMap<Long, MySession>();
    }

}


