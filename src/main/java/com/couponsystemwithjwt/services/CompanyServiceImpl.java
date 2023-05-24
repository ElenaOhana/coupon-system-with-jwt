package com.couponsystemwithjwt.services;

import com.couponsystemwithjwt.entity_beans.Category;
import com.couponsystemwithjwt.entity_beans.Company;
import com.couponsystemwithjwt.entity_beans.Coupon;
import com.couponsystemwithjwt.entity_beans.CustomerPurchase;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.exceptions.ErrMsg;
import com.couponsystemwithjwt.types.ClientStatus;
import com.couponsystemwithjwt.types.CouponStatus;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@Validated
@Getter
@Setter
@ToString
@Scope("prototype")
public class CompanyServiceImpl extends ClientService implements CompanyService {

    private Long companyId;

    public CompanyServiceImpl() {
        super();
    }

    @Override
    public boolean login(String email, String password) throws CouponSystemException {
        if (!companyRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(ErrMsg.INVALID_EMAIL_OR_PASSWORD);
        }
        return true;
    }

    @Override
    public Long loginCompanyReturnId(String email, String password) throws CouponSystemException {
        if (companyRepository.existsByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE)) {
            Long id = companyRepository.findByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE).getId();
            this.companyId = id;
            return id;
        } else {
            throw new CouponSystemException(ErrMsg.CLIENT_DOES_NOT_EXISTS);
        }
    }

    /**
     * The method receives the coupon,
     * checks:
     * 1) if the coupon belongs to received company,
     * 2) if the title of the coupon exists in received company,
     * if company doesn't have the coupon with the same title -
     * - the method adds received coupon,
     * otherwise CouponSystemException is thrown.
     */
    @Override
    public Coupon addCoupon(@NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException {
        if (coupon.getCompany().getId().equals(companyId)) {
            if (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), companyId)) {
                throw new CouponSystemException(ErrMsg.TITLE_EXISTS);
            } else {
                return couponRepository.save(coupon);
            }
        } else
            throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
    }

    @Override
    public Integer updateCoupon(@NotNull(message = "The couponId must not be null") Long couponId,
                                @NotNull(message = "The Coupon must not be null") Coupon coupon) throws CouponSystemException {
        Coupon couponFromDb = couponRepository.getCouponById(couponId);
        if (couponFromDb != null) {
            if (couponFromDb.getCompany().getId().equals(companyId)) {
                if (!couponId.equals(coupon.getId())) {
                    throw new CouponSystemException(ErrMsg.UPDATE_COUPON_ID);
                }
                if (!couponFromDb.getCompany().getId().equals(coupon.getCompany().getId())) {
                    throw new CouponSystemException((ErrMsg.UPDATE_COMPANY_ID_IN_COUPON));
                }
                //additional check as a result from Project requirements to method add()
                if ((!couponFromDb.getTitle().equals(coupon.getTitle())) && (couponRepository.existsByTitleAndCompanyId(coupon.getTitle(), companyId))) {
                    throw new CouponSystemException(ErrMsg.TITLE_EXISTS);
                }
                return couponRepository.updateCoupon(coupon.getCategory(), coupon.getTitle(), coupon.getDescription(), coupon.getStartDate(), coupon.getEndDate(), coupon.getAmount(), coupon.getPrice(), coupon.getImage(), coupon.getId());
            } else {
                throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
            }
        } throw new CouponSystemException(ErrMsg.ID_NOT_FOUND);
    }


    /**
     * By receiving the coupon Id the method checks if that coupon belongs to logged in company,
     * changes status of company coupons to DISABLE,
     * deletes customer purchase from customers_vs_coupons table. If some of those operations did not succeed - the CouponSystemException is thrown.
     * In success returned 1.
     */
    @Override
    public Integer removeCoupon(@NotNull Long id) throws CouponSystemException {
        if (couponRepository.findById(id).isPresent()) {
            Coupon coupon = couponRepository.getCouponById(id);
            if (coupon.getCompany().getId().equals(companyId)) {
                //deletes customer purchase from customers_purchases table
                List<CustomerPurchase> customerPurchases = customerPurchaseRepository.findByCouponId(id);
                if (!customerPurchases.isEmpty()) {
                    customerPurchases.forEach(cp -> customerPurchaseRepository.deleteByCouponId(id));
                }
                //changes status to DISABLE of the coupon
                coupon.setCouponStatus(CouponStatus.DISABLE);
            } else {
                throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
            }
        } else {
            throw new CouponSystemException(ErrMsg.ID_NOT_FOUND);
        }
        return 1;
    }

    @Override
    public Coupon getCouponByIdOfConnectedCompany(Long id) throws CouponSystemException {
        if (!couponRepository.existsByIdAndCompanyId(id, companyId)) {
            throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
        } else {
            return couponRepository.getCouponByIdAndCompanyId(id, companyId);
        }
    }

    @Override
    public Company findById(Long id) throws CouponSystemException {
        return companyRepository.findById(id).orElseThrow(() -> new CouponSystemException(ErrMsg.ID_NOT_FOUND));
    }

    @Override
    public Company getCompanyDetails() {
        return companyRepository.getCompanyById(companyId);
    }

    @Override
    public List<Coupon> getAllCompanyCoupons() {
        return couponRepository.findAllByCompanyId(companyId);
    }

    @Override
    public Coupon getCouponByIdAndTitle(Long id, String title) throws CouponSystemException {
        if (!couponRepository.existsByIdAndTitle(id, title)) {
            throw new CouponSystemException(ErrMsg.NO_ID_AND_TITLE_MATCH);
        } else {
            return couponRepository.getCouponByIdAndTitle(id, title);
        }
    }

    @Override
    public List<Coupon> getCompanyCouponsByCategory(Category category) {
        return couponRepository.findByCategoryAndCompanyId(category, companyId);
    }

    @Override
    public List<Coupon> getCompanyCouponsByCategoryId(Long categoryId) {
        return couponRepository.findByCategoryIdAndCompanyId(categoryId,companyId);
    }

    @Override
    public List<Coupon> getCompanyCouponsByCategoryAndStatus(Category category, CouponStatus couponStatus) {
        return couponRepository.findByCategoryAndCompanyIdAndCouponStatus(category, companyId, couponStatus);
    }

    @Override
    public double getMaxPriceOfCouponsOfCompany() throws CouponSystemException {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new CouponSystemException(ErrMsg.ID_NOT_FOUND));
        return couponRepository.findMaxPrice(company);
    }

    @Override
    public Coupon getMostExpensiveCoupon() throws CouponSystemException {
        List<Coupon> coupons = couponRepository.findAll();
        Coupon expensiveCoupon = coupons.stream().sorted((c1, c2) -> (int) (c2.getPrice() - c1.getPrice())).findFirst().orElseThrow(CouponSystemException::new);
        return expensiveCoupon;
    }

    @Override
    public Coupon getLeastExpensiveCoupon() throws CouponSystemException {
        List<Coupon> coupons = couponRepository.findAll();
        Coupon cheapCoupon = coupons.stream().sorted((c1, c2) -> (int) (c1.getPrice() - c2.getPrice())).findFirst().orElseThrow(() -> new CouponSystemException(ErrMsg.COUPON_DOES_NOT_EXIST));
        return cheapCoupon;
    }


    @Override
    public List<Coupon> findFromCompanyCouponsUpToMaxPrice() throws CouponSystemException {
        double maxPrice = getMaxPriceOfCouponsOfCompany();
        return couponRepository.findFromCouponsOfTheCompany(maxPrice, companyId);
    }

    // My query)) == getExistingCouponsCategories() == that CouponStatus=CouponStatus.ABLE
    @Override
    public Set<Category> getAbleCouponsCategories() {
        return
                couponRepository
                        .findByCouponStatus(CouponStatus.ABLE)
                        .stream()
                        .map(cr -> cr.getCategory())// ==  .map(Coupon::getCategory) - method reference
                        .collect(Collectors.toSet());
    }

    // My query)) == getExistingCouponsCategories() == that CouponStatus=CouponStatus.ABLE
    @Override
    public List<Category> getDrinksCouponsCategories() { // Returns duplicates, so instead of "Distinct" I've done Set<>
        return
                couponRepository
                        .findByDescriptionEndingWith("drinks")
                        .stream()
                        .map(cr -> cr.getCategory())
                        .collect(Collectors.toList());
    }

    @Override
    public Set<Category> getDisableCouponsCategories() {
        return
                couponRepository
                        .findByCouponStatus(CouponStatus.DISABLE)
                        .stream()
                        .map(cr -> cr.getCategory())
                        .collect(Collectors.toSet());
    }

    @Override
    public Coupon getCouponByIdAndCouponStatus(Long id, CouponStatus couponStatus) throws CouponSystemException {
        return couponRepository.findByIdAndCouponStatus(id, couponStatus).orElseThrow(() -> new CouponSystemException(ErrMsg.ID_AND_COUPON_STATUS_NOR_FOUND));
    }

    @Override
    public Company getCompanyByEmailAndPassword(String email, String password) throws CouponSystemException {
        if (!companyRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(ErrMsg.INVALID_EMAIL_OR_PASSWORD);
        }
        return companyRepository.getCompanyByEmailAndPassword(email, password);
    }
}
