package com.couponsystemwithjwt.controllers;

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
    protected HashMap<Long, MySession> sessions;
}
