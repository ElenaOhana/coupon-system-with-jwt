package com.couponsystemwithjwt.controllers;

import com.couponsystemwithjwt.entity_beans.Administrator;
import com.couponsystemwithjwt.entity_beans.Company;
import com.couponsystemwithjwt.entity_beans.Customer;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.security.request.Credentials;
import com.couponsystemwithjwt.services.AdminService;
import com.couponsystemwithjwt.services.ClientService;
import com.couponsystemwithjwt.services.CompanyService;
import com.couponsystemwithjwt.services.CustomerService;
import com.couponsystemwithjwt.state.MySession;
import com.couponsystemwithjwt.types.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/auth")
public class LoginController extends ClientController {

    @Autowired
    private HashMap<Long, MySession> sessions;

    @PostMapping(path = "/login/{type}")
    protected ResponseEntity<?> login(@RequestBody Credentials credentials, @PathVariable ClientType type) throws CouponSystemException {
        ClientService clientService = null;
        try {
            clientService = loginManager.login(credentials.getEmail(), credentials.getPassword(), type);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        long id = 0;
        String token = null;
        try {
            if (clientService instanceof AdminService) {
                id = ((AdminService) clientService).getAdminId();
                Administrator adminByCred = new Administrator(1L, credentials.getEmail(), credentials.getPassword());
                token = tokenManager.createTokenForAdmin(adminByCred);
            } else if (clientService instanceof CompanyService) {
                id = ((CompanyService) clientService).getCompanyId();
                //Company companyFromDbByCred = companyService.getCompanyByEmailAndPassword(credentials.getEmail(), credentials.getPassword());
                Company companyFromDbById = ((CompanyService) clientService).findById(id);
                token = tokenManager.createTokenForCompany(companyFromDbById);
            } else if (clientService instanceof CustomerService) {
                id = ((CustomerService) clientService).getCustomerId();
                //Customer customerFromDbByCred = customerService.getCustomerByEmailAndPassword(credentials.getEmail(), credentials.getPassword());
                Customer customerFromDbById = ((CustomerService) clientService).findById(id);
                token = tokenManager.createTokenForCustomer(customerFromDbById);
            }
        } catch (CouponSystemException e) {
            throw new CouponSystemException(e.getCause());
        }
        sessions.put(id, new MySession(clientService, LocalDateTime.now()));

        /* return generated token to client (token will contain id)*/
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }
}
