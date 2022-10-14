package com.couponsystemwithjwt.controllers;

import com.auth0.jwt.JWT;
import com.couponsystemwithjwt.entity_beans.CustomerPurchase;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.repositories.CouponRepository;
import com.couponsystemwithjwt.security.validTokenAspect.ValidToken;
import com.couponsystemwithjwt.services.CustomerService;
import com.couponsystemwithjwt.types.CouponStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping({"customer"})
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CustomerController extends ClientController {

    @Autowired
    CouponRepository couponRepository;


    @PostMapping("customer_purchase")
    @ValidToken
    public ResponseEntity<?> purchaseCoupon(HttpServletRequest request, @RequestBody CustomerPurchase customerPurchase) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return ResponseEntity.ok(customerService.createCustomerPurchase(customerPurchase));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("customer_purchase")
    @ValidToken
    public ResponseEntity<?> getCustomerPurchases(HttpServletRequest request) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return new ResponseEntity<>(customerService.getCustomerPurchases(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("purchase_by_category/{categoryId}")
    @ValidToken
    public ResponseEntity<?> getCustomerPurchasesOfConnectedCustomerByCategoryId(HttpServletRequest request, @PathVariable long categoryId) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (customerService != null) {
            return new ResponseEntity<>(customerService.getCustomerPurchasesOfConnectedCustomerByCategoryId(categoryId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("max_price_customer")
    @ValidToken
    public ResponseEntity<?> findMaxPriceOfCustomer(HttpServletRequest request) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (customerService != null) {
            return new ResponseEntity<>(customerService.findMaxPriceOfCustomer(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("purchases_less_than_max_price")
    @ValidToken
    public ResponseEntity<?> getCouponListLessThanMaxPrice(HttpServletRequest request) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (customerService != null) {
            return new ResponseEntity<>(customerService.getCouponListLessThanMaxPrice(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("details")
    @ValidToken
    public ResponseEntity<?> getCustomerDetails(HttpServletRequest request) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (customerService != null) {
            return new ResponseEntity<>(customerService.getCustomerDetails(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/coupons-for-all")
    public ResponseEntity<?> getAllCoupons(HttpServletRequest request) throws SecurityException {
        return new ResponseEntity<>(couponRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/able-coupons-for-all")
    public ResponseEntity<?> getAllAbleCoupons(HttpServletRequest request) throws SecurityException {
        return new ResponseEntity<>(couponRepository.findAllByCouponStatus(CouponStatus.ABLE), HttpStatus.OK);
    }
}
