package com.couponsystemwithjwt.exceptions;

public enum ErrMsg {
    COMPANY_NAME_EXISTS("The company name is exist already"),
    COMPANY_EMAIL_EXISTS("The company email is exist already"),
    COMPANY_EXISTS("The company with those email and name exists already"),
    ID_NOT_FOUND("Id not found"),
    UPDATE_COMPANY_ID("It is not allowed to change the company id."),
    UPDATE_COMPANY_NAME("It is not allowed to change the company name."),
    UPDATE_CUSTOMER_ID("It is not allowed to change the customer id."),
    UPDATE_ID("It is not allowed to change the company id."),
    UPDATE_NAME("It is not allowed to change the company name."),
    UPDATE_NO_MATCH("There is no matching between company id and name. (There is no company like you conveyed.)"),
    NO_ID_AND_TITLE_MATCH("There is no matching between coupon id and title"),

    TITLE_NOT_FOUND("The title not found"),
    TITLE_EXISTS("The coupon with this title exists already"),
    CUSTOMER_MAIL_EXISTS("Customer email is exist already"),

    IS_NOT_POSSIBLE_TO_CHANGE_ID("Is not possible to change an existing customer id, OR you trying update data at different entities"),

    CUSTOMER_NAME_DOES_NOT_EXIST_IN_DB("Customer with this name does not exists in the database"),

    ILLEGAL_ACTION("The action is illegal"),
    BAD_REQUEST("Bad request"),
    ID_AND_COUPON_STATUS_NOR_FOUND("There is no matching between provided id with the coupon status"),
    UPDATE_COUPON_ID("It is not allowed to change the coupon id"),
    UPDATE_COMPANY_ID_IN_COUPON("It is not allowed to change the company id in coupon."),
    NEGATIVE_COUPON_AMOUNT("Not enough coupons to purchase."),
    COUPON_OVERDUE("The coupon end date is expired"),
    COUPON_HAD_BOUGHT("You have bought the coupon already"),
    INVALID_EMAIL_OR_PASSWORD("Invalid email or password"),
    COUPON_STATUS_DISABLE("The coupon has status DISABLE, what means coupon does not exist"),
    COUPON_DOES_NOT_EXIST("The coupon does not exist"),
    CLIENT_DOES_NOT_EXISTS("Client does not exists"),
    SESSION_NOT_FOUND("Session does not exist");

    private String errDescription;

    ErrMsg(String description) {
        this.errDescription = description;
    }

    public String getErrDescription() {
        return errDescription;
    }

    public void setErrDescription(String errDescription) {
        this.errDescription = errDescription;
    }
}
