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
public class LoginManager {
    private ApplicationContext context;

    public ApplicationContext getContext() {
        return context;
    }

    public LoginManager(ApplicationContext context) {
        this.context = context;
    }

    /*
     * The method login the client:
     * the method checks by email and password param if client exists, and by clientType param which ClientFacade should return.
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
                     companyService.loginCompanyReturnId(email, password); // companyService with companyId=1
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
}
