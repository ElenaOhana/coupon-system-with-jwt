package com.couponsystemwithjwt.controllers;

import com.auth0.jwt.JWT;
import com.couponsystemwithjwt.entity_beans.CustomerPurchase;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.repositories.CompanyRepository;
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
    public ResponseEntity<?> purchaseCoupon(HttpServletRequest request, @RequestBody CustomerPurchase customerPurchase) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(customerService.createCustomerPurchase(customerPurchase));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("customer_purchase")
    @ValidToken
    public ResponseEntity<?> getCustomerPurchases(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(customerService.getCustomerPurchases(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("purchase_by_category/{categoryId}")
    @ValidToken
    public ResponseEntity<?> getCustomerPurchasesOfConnectedCustomerByCategoryId(HttpServletRequest request, @PathVariable long categoryId) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(customerService.getCustomerPurchasesOfConnectedCustomerByCategoryId(categoryId), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("max_price_customer")
    @ValidToken
    public ResponseEntity<?> findMaxPriceOfCustomer(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(customerService.findMaxPriceOfCustomer(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("purchases_less_than_max_price")
    @ValidToken
    public ResponseEntity<?> getCouponListLessThanMaxPrice(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(customerService.getCouponListLessThanMaxPrice(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("details")
    @ValidToken
    public ResponseEntity<?> getCustomerDetails(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CustomerService customerService = (CustomerService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(customerService.getCustomerDetails(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/coupons-for-all")
    public ResponseEntity<?> getAllCoupons(HttpServletRequest request) throws SecurityException {
        return new ResponseEntity<>(couponRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/able-coupons-for-all")
    public ResponseEntity<?> getAllAbleCoupons(HttpServletRequest request) throws SecurityException {
        return new ResponseEntity<>(couponRepository.findAllByCouponStatus(CouponStatus.ABLE), HttpStatus.OK);
    }

    //For view the details of coupon on Home page
    @GetMapping("couponForAllUsers/{couponId}")
    public ResponseEntity<?> getOneCoupon(HttpServletRequest request, @PathVariable long couponId) {
        return new ResponseEntity<>(couponRepository.getCouponById(couponId), HttpStatus.OK);
    }
}

