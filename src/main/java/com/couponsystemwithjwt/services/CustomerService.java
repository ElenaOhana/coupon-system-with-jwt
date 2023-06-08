package com.couponsystemwithjwt.services;

import com.couponsystemwithjwt.entity_beans.Coupon;
import com.couponsystemwithjwt.entity_beans.Customer;
import com.couponsystemwithjwt.entity_beans.CustomerPurchase;
import com.couponsystemwithjwt.exceptions.CouponSystemException;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CustomerService {

    Long loginCustomerReturnId(String email, String password) throws CouponSystemException;
    CustomerPurchase createCustomerPurchase(@NotNull CustomerPurchase customerPurchase) throws CouponSystemException;

    List<CustomerPurchase> getCustomerPurchases();
    List<Coupon> getCustomerPurchasesOfConnectedCustomerByCategoryId(Long categoryId);
    Double findMaxPriceOfCustomer() throws CouponSystemException;
    List<Coupon> getCouponListLessThanMaxPrice() throws CouponSystemException;

    Customer getCustomerDetails();

    Customer getCustomerByEmailAndPassword(String email, String password) throws CouponSystemException;

    CustomerPurchase getCustomerPurchaseByCouponId(Long couponId) throws CouponSystemException;

    Long getCustomerId();

    Customer findById(long id) throws CouponSystemException;
    Customer addCustomer(String name, String surname, String email, String password) throws CouponSystemException;
}

