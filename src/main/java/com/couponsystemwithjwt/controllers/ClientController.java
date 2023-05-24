package com.couponsystemwithjwt.controllers;

import com.couponsystemwithjwt.repositories.CategoryRepository;
import com.couponsystemwithjwt.security.LoginManager;
import com.couponsystemwithjwt.security.TokenManager;
import com.couponsystemwithjwt.services.*;
import com.couponsystemwithjwt.state.MySession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public abstract class ClientController {

    @Autowired
    protected TokenManager tokenManager;

    @Autowired
    protected LoginManager loginManager;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    protected HashMap<Long, MySession> sessions;

}
