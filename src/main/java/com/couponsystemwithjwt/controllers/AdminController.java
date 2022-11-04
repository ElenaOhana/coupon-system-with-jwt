package com.couponsystemwithjwt.controllers;

import com.auth0.jwt.JWT;
import com.couponsystemwithjwt.entity_beans.Company;
import com.couponsystemwithjwt.entity_beans.Customer;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.security.validTokenAspect.ValidToken;
import com.couponsystemwithjwt.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Transactional
@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class AdminController extends ClientController {

    public AdminController() {
        super();
    }

    @GetMapping("company")
    @ValidToken
    public ResponseEntity<?> getAllCompanies(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(adminService.getAllCompanies(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/company/{companyId}")
    @ValidToken
    public ResponseEntity<?> getCompany(@PathVariable long companyId, HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.getOneCompany(companyId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("company/{companyId}")
    @ValidToken
    public ResponseEntity<?> updateCompanyById(@PathVariable long companyId, @RequestBody Company company, HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.updateCompanyById(companyId, company));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("company")
    @ValidToken
    public ResponseEntity<?> addCompany(@RequestBody Company company, HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.addCompany2(company));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("company/{companyId}")
    //@ValidToken
    public ResponseEntity<?> deleteCompany(HttpServletRequest request, @PathVariable long companyId) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.deleteCompanyAsChangeStatus(companyId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // CUSTOMER
    @GetMapping("customer")
    @ValidToken
    public ResponseEntity<?> getAllCustomers(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(adminService.getAllCustomers(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("customer/{customerId}")
    @ValidToken
    public ResponseEntity<?> getCustomer(HttpServletRequest request, @PathVariable long customerId) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.getOneCustomer(customerId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("customer/{customerId}")
    @ValidToken
    public ResponseEntity<?> updateCustomerById(HttpServletRequest request, @PathVariable long customerId, @RequestBody Customer customer) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.updateCustomer(customerId, customer));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("customer")
    @ValidToken
    public ResponseEntity<?> addCustomer(HttpServletRequest request, @RequestBody Customer customer) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.addCustomer(customer));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("customer/{customerId}")
    @ValidToken
    public ResponseEntity<?> deleteCustomer(HttpServletRequest request, @PathVariable long customerId) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            AdminService adminService = (AdminService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(adminService.deleteCustomerAsChangeStatus(customerId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
