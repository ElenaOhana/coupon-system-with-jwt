package com.couponsystemwithjwt.services;

import com.couponsystemwithjwt.entity_beans.Category;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Created by Elena on 30 June, 2021
 */

public abstract class ClientService {

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected CouponRepository couponRepository;

    @Autowired
    protected CustomerPurchaseRepository customerPurchaseRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    public abstract boolean login(String email, String password) throws CouponSystemException;

}
