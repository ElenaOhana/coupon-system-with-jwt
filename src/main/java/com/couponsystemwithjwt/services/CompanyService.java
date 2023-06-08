package com.couponsystemwithjwt.services;

import com.couponsystemwithjwt.entity_beans.Category;
import com.couponsystemwithjwt.entity_beans.Company;
import com.couponsystemwithjwt.entity_beans.Coupon;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.types.CouponStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public interface CompanyService {

    Long loginCompanyReturnId(String email, String password) throws CouponSystemException;
    Coupon addCoupon(@NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException;//For CompanyServiceImpl as a Singleton and PROTOTYPE

    Integer updateCoupon(@NotNull(message = "The couponId must not be null") Long couponId,
                      @NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException;

    Integer removeCoupon(@NotNull Long id) throws CouponSystemException;
    List<Coupon> getAllCompanyCoupons();
    Coupon getCouponByIdOfConnectedCompany(Long id) throws CouponSystemException;
    Company findById(Long id) throws CouponSystemException;
    Company getCompanyDetails();
    Coupon getCouponByIdAndTitle(Long id, String title) throws CouponSystemException;
    List<Coupon> getCompanyCouponsByCategory(Category category);
    List<Coupon> getCompanyCouponsByCategoryAndStatus(Category category, CouponStatus couponStatus);
    Double getMaxPriceOfCouponsOfCompany() throws CouponSystemException ;
    List<Coupon> findFromCompanyCouponsUpToMaxPrice() throws CouponSystemException;


    Set<Category> getAbleCouponsCategories();
    List<Category> getDrinksCouponsCategories();
    Set<Category> getDisableCouponsCategories();
    Coupon getCouponByIdAndCouponStatus(Long id, CouponStatus couponStatus) throws CouponSystemException;

    Long getCompanyId();

    Company getCompanyByEmailAndPassword(String email, String password) throws CouponSystemException;
    Coupon getMostExpensiveCoupon() throws CouponSystemException;
    Coupon getLeastExpensiveCoupon() throws CouponSystemException;

    List<Coupon> getCompanyCouponsByCategoryId(Long categoryId);

    Company addCompany(String name, String email, String password) throws CouponSystemException;
}
