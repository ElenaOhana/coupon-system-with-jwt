package com.couponsystemwithjwt.clr;

import com.couponsystemwithjwt.entity_beans.Category;
import com.couponsystemwithjwt.entity_beans.Coupon;
import com.couponsystemwithjwt.entity_beans.CustomerPurchase;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.repositories.*;
import com.couponsystemwithjwt.security.LoginManager;
import com.couponsystemwithjwt.services.CustomerServiceImpl;
import com.couponsystemwithjwt.types.ClientType;
import com.couponsystemwithjwt.types.CouponStatus;
import com.couponsystemwithjwt.utils.ArtUtils;
import com.couponsystemwithjwt.utils.ConsoleColors;
import com.couponsystemwithjwt.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Order(4)
@Component
public class CustomerServiceTest implements CommandLineRunner {

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerPurchaseRepository customerPurchaseRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + ArtUtils.CUSTOMER_SERVICE_TEST);
        TestUtils.printTest("bad logging");
        try {
            System.out.println(loginManager.login("some_bad_mail@gmail.com", "1234", ClientType.CUSTOMER));
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }
        CustomerServiceImpl customerServiceImpl = null;
        TestUtils.printTest("good logging");
        System.out.println(customerServiceImpl = (CustomerServiceImpl) loginManager.login("anna_an44@Gmail.com", "5555", ClientType.CUSTOMER));

        CustomerPurchase customerPurchaseAsDummyData2 = CustomerPurchase.builder()
                .customer(customerRepository.getCustomerById(2L))
                .coupon(couponRepository.getCouponById(5L))
                .couponTitle(couponRepository.getById(5L).getTitle())
                //.couponTitle(couponRepository.findById(5L).get().getTitle())
                .customerName(customerRepository.getById(2L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();
        customerPurchaseRepository.save(customerPurchaseAsDummyData2);

        TestUtils.printTest(("Customer Purchase will fail due to the non-connected customer trying to buy the coupon"));
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData2);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Customer Purchase will fail due to trying to buy the same coupon"));
        CustomerPurchase existingCustomerPurchase = customerPurchaseRepository.findByCustomerIdAndCouponId(3L, 3L);
        try {
            customerServiceImpl.createCustomerPurchase(existingCustomerPurchase);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }


        TestUtils.printTest(("Customer Purchase will fail due to trying to buy the coupon with amount 0"));
        Coupon couponAsDummyDataCoupon = Coupon.builder()
                .company(companyRepository.getById(4L))
                .category(categoryRepository.getById(4L))
                .title("Skiing deal")
                .description("Finest Andora ski")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .amount(0)
                .price(3800)
                .image("https://picsum.photos/200")
                .couponStatus(CouponStatus.ABLE)
                .build();
        couponRepository.save(couponAsDummyDataCoupon);
        CustomerPurchase customerPurchaseAsDummyData = CustomerPurchase.builder()
                .id(7L)
                .customer(customerRepository.getCustomerById(3L))
                .coupon(couponRepository.getCouponByTitle(couponAsDummyDataCoupon.getTitle()))
                .couponTitle(couponAsDummyDataCoupon.getTitle())
                .customerName(customerRepository.getById(3L).getFirstName())
                .purchaseDateTime(LocalDateTime.now())
                .build();
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Customer Purchase will fail due to trying to buy the expired coupon"));
        Category category4 = categoryRepository.getById(4L);
        Coupon couponByCategoryFromDB = couponRepository.getByCategory(category4);
        couponByCategoryFromDB.setEndDate(LocalDateTime.now().minusDays(1));
        couponByCategoryFromDB.setAmount(5); // changes in Heap only
        couponRepository.save(couponByCategoryFromDB);
        customerPurchaseAsDummyData.setCoupon(couponByCategoryFromDB);
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Customer Purchase will succeed"));
        couponByCategoryFromDB.setEndDate(LocalDateTime.now().plusDays(14));
        couponRepository.save(couponByCategoryFromDB);
        customerPurchaseAsDummyData.setCoupon(couponByCategoryFromDB);
        try {
            customerServiceImpl.createCustomerPurchase(customerPurchaseAsDummyData);
            System.out.println("The coupon "+ customerPurchaseAsDummyData.getCoupon().getId() +" that customer bought is: "+couponRepository.getByCategory(category4));
            System.out.println(customerPurchaseAsDummyData);
        } catch (CouponSystemException e) {
            System.out.println(e.getMessage());
        }

        TestUtils.printTest(("Get all Customer Purchases of connected customer will succeed"));
        List<CustomerPurchase> customerPurchases = customerServiceImpl.getCustomerPurchases();
        customerPurchases.forEach(System.out::println);


        TestUtils.printTest(("Get all Customer Purchases of connected customer by Category will succeed"));
        Category travelling = categoryRepository.getById(4L);
        List<Coupon> customerPurchasesByCategory = customerServiceImpl.getCustomerPurchasesOfConnectedCustomerByCategoryId( travelling.getId());
        customerPurchasesByCategory.forEach(System.out::println);

        TestUtils.printTest("Get Max Price of connected customer will succeed");
        System.out.println(customerServiceImpl.findMaxPriceOfCustomer());

        TestUtils.printTest("Get coupons of connected customer up to max coupon purchases price will succeed");
        List<Coupon> couponListUpToMaxPriceOfCustomer = customerServiceImpl.getCouponListLessThanMaxPrice();
        couponListUpToMaxPriceOfCustomer.forEach(System.out::println);

        TestUtils.printTest(("Get connected customer details will succeed"));
        System.out.println(customerServiceImpl.getCustomerDetails());
    }
}
