package com.couponsystemwithjwt.controllers;

import com.auth0.jwt.JWT;
import com.couponsystemwithjwt.entity_beans.Category;
import com.couponsystemwithjwt.entity_beans.Coupon;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.security.validTokenAspect.ValidToken;
import com.couponsystemwithjwt.services.CompanyService;
import com.couponsystemwithjwt.types.CouponStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("company")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class CompanyController extends ClientController {

    public CompanyController() {
        super();
    }

    @GetMapping("coupon")
    @ValidToken
    public ResponseEntity<?> getAllCoupons(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (companyService != null) {
            return new ResponseEntity<>(companyService.getAllCompanyCoupons(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("coupon/{couponId}")
    @ValidToken
    public ResponseEntity<?> getOneCoupon(HttpServletRequest request, @PathVariable long couponId) {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return ResponseEntity.ok(companyService.getCouponByIdOfConnectedCompany(couponId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("coupon")
    @ValidToken
    public ResponseEntity<?> addCoupon(HttpServletRequest request, @RequestBody Coupon coupon) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return ResponseEntity.ok(companyService.addCoupon(coupon));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("coupon/{couponId}")
    @ValidToken
    public ResponseEntity<?> updateCoupon(HttpServletRequest request, @PathVariable long couponId, @RequestBody Coupon coupon) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return ResponseEntity.ok(companyService.updateCoupon(couponId, coupon));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("coupon/{couponId}")
    @ValidToken
    public ResponseEntity<?> deleteCouponById(HttpServletRequest request, @PathVariable long couponId) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return ResponseEntity.ok(companyService.removeCoupon(couponId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("details")
    @ValidToken
    public ResponseEntity<?> getCompanyDetails(HttpServletRequest request) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (companyService != null) {
            return new ResponseEntity<>(companyService.getCompanyDetails(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("category")
    @ValidToken
    public ResponseEntity<?> getCompanyCouponsByCategory(HttpServletRequest request, @RequestBody Category category) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (companyService != null) {
            return new ResponseEntity<>(companyService.getCompanyCouponsByCategory(category), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("category-and-couponStatus/{couponStatus}") // @PathVariable
    @ValidToken
    public ResponseEntity<?> getCompanyCouponsByCategoryAndStatus(HttpServletRequest request, @RequestBody Category category, @PathVariable CouponStatus couponStatus) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        if (companyService != null) {
            return new ResponseEntity<>(companyService.getCompanyCouponsByCategoryAndStatus(category, couponStatus), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("max_price")
    @ValidToken
    public ResponseEntity<?> getMaxPriceOfCouponsOfCompany(HttpServletRequest request) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return ResponseEntity.ok(companyService.getMaxPriceOfCouponsOfCompany());
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("coupons_less_than_max_price")
    @ValidToken
    public ResponseEntity<?> getCouponListLessThanMaxPrice(HttpServletRequest request) throws SecurityException {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
        try {
            return new ResponseEntity<>(companyService.findFromCompanyCouponsUpToMaxPrice(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
