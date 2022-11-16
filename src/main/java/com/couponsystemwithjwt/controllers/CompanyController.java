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

 /*   @GetMapping("category/{categoryName}")
    @ValidToken
    public ResponseEntity<?> getCategory(HttpServletRequest request, @PathVariable String categoryName) {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            Category category =categoryRepository.findByName(categoryName);
            System.out.println(category);
            return ResponseEntity.ok(categoryRepository.findByName(categoryName));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }*/

    @GetMapping("coupon")
    @ValidToken
    public ResponseEntity<?> getAllCoupons(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(companyService.getAllCompanyCoupons(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("coupon/{couponId}")
    @ValidToken
    public ResponseEntity<?> getOneCoupon(HttpServletRequest request, @PathVariable long couponId) {
        String token = tokenManager.returnPureToken(request);
        long id;
        id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(companyService.getCouponByIdOfConnectedCompany(couponId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/coupon")
    //@ValidToken
    public ResponseEntity<?> addCoupon(HttpServletRequest request, @RequestBody Coupon coupon) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
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
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(companyService.updateCoupon(couponId, coupon));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("coupon/{couponId}")
    @ValidToken
    public ResponseEntity<?> deleteCouponById(HttpServletRequest request, @PathVariable long couponId) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(companyService.removeCoupon(couponId));
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("details")
    @ValidToken
    public ResponseEntity<?> getCompanyDetails(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(companyService.getCompanyDetails(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("category")
    @ValidToken
    public ResponseEntity<?> getCompanyCouponsByCategory(HttpServletRequest request, @RequestBody Category category) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(companyService.getCompanyCouponsByCategory(category), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("category/{categoryId}")
    @ValidToken
    public ResponseEntity<?> getCompanyCouponsByCategoryId(HttpServletRequest request, @PathVariable Long categoryId) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(companyService.getCompanyCouponsByCategoryId(categoryId), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("category-and-couponStatus/{couponStatus}")
    @ValidToken
    public ResponseEntity<?> getCompanyCouponsByCategoryAndStatus(HttpServletRequest request, @RequestBody Category category, @PathVariable CouponStatus couponStatus) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(companyService.getCompanyCouponsByCategoryAndStatus(category, couponStatus), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @GetMapping("max_price")
    @ValidToken
    public ResponseEntity<?> getMaxPriceOfCouponsOfCompany(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return ResponseEntity.ok(companyService.getMaxPriceOfCouponsOfCompany());
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("coupons_less_than_max_price")
    @ValidToken
    public ResponseEntity<?> getCouponListLessThanMaxPrice(HttpServletRequest request) {
        String token = tokenManager.returnPureToken(request);
        long id = JWT.decode(token).getClaim("id").asLong();
        try {
            CompanyService companyService = (CompanyService) tokenManager.getClientFromSessionByTokenIdAndSetLastActive(id);
            return new ResponseEntity<>(companyService.findFromCompanyCouponsUpToMaxPrice(), HttpStatus.OK);
        } catch (CouponSystemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
