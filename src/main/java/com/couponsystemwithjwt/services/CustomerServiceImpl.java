package com.couponsystemwithjwt.services;

import com.couponsystemwithjwt.entity_beans.Coupon;
import com.couponsystemwithjwt.entity_beans.Customer;
import com.couponsystemwithjwt.entity_beans.CustomerPurchase;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.exceptions.ErrMsg;
import com.couponsystemwithjwt.types.ClientStatus;
import com.couponsystemwithjwt.types.CouponStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
@Validated
@Data
@Scope("prototype")
public class CustomerServiceImpl extends ClientService implements CustomerService {

    private Long customerId;

    public CustomerServiceImpl() {
        super();
    }

    @Override
    public Long getCustomerId() {
        return customerId;
    }

    @Override
    public Customer findById(long id) throws CouponSystemException {
        return customerRepository.findById(id).orElseThrow(() -> new CouponSystemException(ErrMsg.ID_NOT_FOUND));
    }

    @Override
    public boolean login(String email, String password) throws CouponSystemException {
        if (!customerRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(ErrMsg.INVALID_EMAIL_OR_PASSWORD);
        }
        return true;
    }

    @Override
    public Long loginCustomerReturnId(String email, String password) throws CouponSystemException {
        if (customerRepository.existsByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE)) {
            Long id = customerRepository.findByEmailAndPasswordAndClientStatus(email, password, ClientStatus.ACTIVE).getId();
            this.customerId = id;
            return id;
        } else {
            throw new CouponSystemException(ErrMsg.CLIENT_DOES_NOT_EXISTS);
        }
    }

    /**
     * Work in front of CustomerPurchaseRepository
     */
    @Override
    public CustomerPurchase createCustomerPurchase(@NotNull CustomerPurchase customerPurchase) throws CouponSystemException {
        Long couponId = customerPurchase.getCoupon().getId();
        Coupon couponFromDb = couponRepository.getCouponById(couponId);
        if (couponFromDb != null) {
            CustomerPurchase newCustomerPurchase = CustomerPurchase.builder()
                    .customer(customerPurchase.getCustomer())
                    .coupon(couponFromDb)
                    .couponTitle(couponFromDb.getTitle())
                    .customerName(customerPurchase.getCustomerName())
                    .purchaseDateTime(LocalDateTime.now())
                    .build();

            if (customerPurchase.getCustomer().getId() != null) {
                if (customerPurchase.getCustomer().getId().equals(customerId)) {
                    if (couponFromDb.getAmount() <= 0) {
                        throw new CouponSystemException(ErrMsg.NEGATIVE_COUPON_AMOUNT);
                    }
                    if (couponFromDb.getEndDate().isBefore(LocalDateTime.now())) {
                        throw new CouponSystemException(ErrMsg.COUPON_OVERDUE);
                    }
                    if (customerPurchaseRepository.existsByCustomerIdAndCouponId(customerPurchase.getCustomer().getId(), couponFromDb.getId())) {
                        throw new CouponSystemException(ErrMsg.COUPON_HAD_BOUGHT);
                    }
                    if (couponFromDb.getCouponStatus().equals(CouponStatus.DISABLE)) {
                        throw new CouponSystemException(ErrMsg.COUPON_STATUS_DISABLE);
                    }
                    int newAmount = couponFromDb.getAmount();
                    newAmount--;
                    couponFromDb.setAmount(newAmount);
                    couponRepository.updateCoupon(couponFromDb.getCategory(), couponFromDb.getTitle(),
                            couponFromDb.getDescription(), couponFromDb.getStartDate(), couponFromDb.getEndDate(),
                            couponFromDb.getAmount(), couponFromDb.getPrice(), couponFromDb.getImage(), couponFromDb.getId());
                    newCustomerPurchase.setCoupon(couponFromDb);
                    customerPurchaseRepository.save(newCustomerPurchase);
                    return newCustomerPurchase;
                }
            }throw new CouponSystemException(ErrMsg.ILLEGAL_ACTION);
        }throw new CouponSystemException(ErrMsg.ID_NOT_FOUND);
    }

    @Override
    public List<CustomerPurchase> getCustomerPurchases() {
        return customerPurchaseRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Coupon> getCustomerPurchasesOfConnectedCustomerByCategoryId(Long categoryId) {
        return couponRepository.findByCustomerIdAndCategoryId(customerId, categoryId);
    }

    @Override
    public Double findMaxPriceOfCustomer() throws CouponSystemException {
        //Optional<Double> maxPrice;
        return couponRepository.findMaxPrice(customerId).orElseThrow(() -> new CouponSystemException(ErrMsg.MAX_PRICE_NOT_FOUND));
        /*try {
            maxPrice = Optional.ofNullable(couponRepository.findMaxPrice(customerId).orElseThrow(() -> new CouponSystemException(ErrMsg.MAX_PRICE_NOT_FOUND)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            maxPrice = -1;
        }*/
    }

    @Override
    public List<Coupon> getCouponListLessThanMaxPrice() throws CouponSystemException {
        List<CustomerPurchase> customerPurchases = customerPurchaseRepository.findByCustomerId(customerId);
        List<Coupon> couponResultList = new ArrayList<>();
        for (CustomerPurchase customerPurchase : customerPurchases) {
            double priceOfDBCouponsOfCustomer = customerPurchase.getCoupon().getPrice();
            if (priceOfDBCouponsOfCustomer < findMaxPriceOfCustomer()) {
                couponResultList.add(customerPurchase.getCoupon());
            }
        }
        return couponResultList;
    }

    @Override
    public Customer getCustomerDetails() {
        return customerRepository.getCustomerById(customerId);
    }

    @Override
    public Customer getCustomerByEmailAndPassword(String email, String password) throws CouponSystemException {
        if (!customerRepository.existsByEmailAndPassword(email, password)) {
            throw new CouponSystemException(ErrMsg.INVALID_EMAIL_OR_PASSWORD);
        }
        return customerRepository.findCustomerByEmailAndPassword(email, password);
    }

    @Override
    public CustomerPurchase getCustomerPurchaseByCouponId(Long couponId) {
        return customerPurchaseRepository.findByCustomerIdAndCouponId(customerId, couponId);
    }

    /**
     * This is the method for register signup: receives the customer params and checks by customer email if the customer exists in Database,
     * if customer doesn't exist - the method adds the customer, and initializes the customerId.
     * otherwise throws CouponSystemException.
     *
     */
    @Override
    public Customer addCustomer(String firstName, String lastName, String email, String password) throws CouponSystemException {
        if (customerRepository.existsByEmail(email)) {
            throw new CouponSystemException(ErrMsg.CUSTOMER_MAIL_EXISTS);
        }
        Customer customer = new Customer(firstName, lastName, email, password);
        Customer customerFromDb = customerRepository.save(customer);
        this.customerId = customerFromDb.getId();
        return customerFromDb;
    }
}
