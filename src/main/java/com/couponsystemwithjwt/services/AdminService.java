package com.couponsystemwithjwt.services;

import com.couponsystemwithjwt.entity_beans.Company;
import com.couponsystemwithjwt.entity_beans.Customer;
import com.couponsystemwithjwt.exceptions.CouponSystemException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface AdminService {
     boolean isCompanyExistsByEmailAndName(String email, String name);
     //boolean isCompanyExistsByEmailAndPassword(String email, String password);

     Company addCompany2(@NotNull Company company) throws CouponSystemException;

     Integer updateCompany(@NotNull Company company) throws CouponSystemException;
     Integer updateCompanyById(@NotNull Long companyId, @NotNull Company company) throws CouponSystemException;

     /**
      * This method changes company status to INACTIVE. */
     Integer deleteCompanyAsChangeStatus(@NotNull Long companyID) throws CouponSystemException;

    /* Returns only ACTIVE companies*/
     List<Company> getAllCompanies();
     Company getOneCompany(@NotNull Long id) throws CouponSystemException;

     Customer addCustomer(@NotNull Customer customer) throws CouponSystemException;
     Integer updateCustomerByName(@NotNull Customer customer, @NotBlank(message = "byName parameter must not be blank") String byName) throws  CouponSystemException;
     Integer updateCustomer(@NotNull Long customerId, @NotNull Customer customer) throws  CouponSystemException;
     Integer deleteCustomerAsChangeStatus(@NotNull Long customerID) throws CouponSystemException;

     /* Returns only ACTIVE customers */
     List<Customer> getAllCustomers();
     Customer getOneCustomer(@NotNull Long customerID) throws CouponSystemException;

     long getAdminId();

}
