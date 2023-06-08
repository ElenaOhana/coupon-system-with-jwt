package com.couponsystemwithjwt.security;

import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.exceptions.ErrMsg;
import com.couponsystemwithjwt.services.AdminServiceImpl;
import com.couponsystemwithjwt.services.ClientService;
import com.couponsystemwithjwt.services.CompanyServiceImpl;
import com.couponsystemwithjwt.services.CustomerServiceImpl;
import com.couponsystemwithjwt.types.ClientType;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AuthManager {
    private ApplicationContext context;

    public ApplicationContext getContext() {
        return context;
    }

    public AuthManager(ApplicationContext context) {
        this.context = context;
    }

    /*
     * The method login the client:
     * the method checks by email and password param if client exists, and checks by clientType param which ClientFacade should return.
     * returns an abstract client facade if the client exists(has the correct credentials) and has correct client type, otherwise throws CouponSystemException.
     */
    public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {
        switch (clientType) {
            case ADMINISTRATOR:
                AdminServiceImpl adminService = context.getBean(AdminServiceImpl.class);
                if (adminService.login(email, password)) {
                    return adminService;
                }
                break;
            case COMPANY:
                CompanyServiceImpl companyService = context.getBean(CompanyServiceImpl.class); // companyService with companyId=null
                if (companyService.login(email, password)) { // my way to check credentials.
                     companyService.loginCompanyReturnId(email, password); // companyService with existed companyId
                    return companyService;
                }
                break;
            case CUSTOMER:
                CustomerServiceImpl customerService = context.getBean(CustomerServiceImpl.class);
                if (customerService.loginCustomerReturnId(email, password) > 0) { // second way to check credentials.
                    return customerService;
                }
                break;
        }
        throw new CouponSystemException(ErrMsg.BAD_REQUEST);
    }

    /*
     * The method signUp the client:
     * returns an abstract client facade according to clientType, otherwise throws CouponSystemException.
     */
    public ClientService signUp(String name, String surname, String email, String password, ClientType clientType) throws CouponSystemException {
        switch (clientType) {
            case CUSTOMER:
                CustomerServiceImpl customerService = context.getBean(CustomerServiceImpl.class);
                customerService.addCustomer(name, surname, email, password);
                return customerService;

            case COMPANY:
                CompanyServiceImpl companyService = context.getBean(CompanyServiceImpl.class); // companyService with companyId=null
                companyService.addCompany(name, email, password); // companyService with existed companyId
                return companyService;

        }
        throw new CouponSystemException(ErrMsg.BAD_REQUEST);
    }
}
