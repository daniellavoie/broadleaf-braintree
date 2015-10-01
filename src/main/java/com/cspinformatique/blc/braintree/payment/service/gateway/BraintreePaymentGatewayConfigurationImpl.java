package com.cspinformatique.blc.braintree.payment.service.gateway;

import org.broadleafcommerce.common.payment.PaymentGatewayType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;

@ConfigurationProperties(prefix = "braintree")
public class BraintreePaymentGatewayConfigurationImpl implements BraintreePaymentGatewayConfiguration {
	private String environment;
	private String merchantId;
	private String publicKey;
	private String privateKey;
	private int failureReportingThreshold = 1;
	private boolean performAuthorizeAndCapture = true;

	@Override
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	@Override
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@Override
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public int getFailureReportingThreshold() {
		return failureReportingThreshold;
	}

	public void setFailureReportingThreshold(int failureReportingThreshold) {
		this.failureReportingThreshold = failureReportingThreshold;
	}

	@Override
	public boolean isPerformAuthorizeAndCapture() {
		return performAuthorizeAndCapture;
	}

	public void setPerformAuthorizeAndCapture(boolean performAuthorizeAndCapture) {
		this.performAuthorizeAndCapture = performAuthorizeAndCapture;
	}

	@Override
	public String getTransparentRedirectUrl() {
		return "/braintree-checkout/process";
	}

	@Override
	public String getTransparentRedirectReturnUrl() {
		return "/braintree-checkout/return";
	}

	@Override
	public boolean handlesAuthorize() {
		return true;
	}

	@Override
	public boolean handlesCapture() {
		return false;
	}

	@Override
	public boolean handlesAuthorizeAndCapture() {
		return true;
	}

	@Override
	public boolean handlesReverseAuthorize() {
		return false;
	}

	@Override
	public boolean handlesVoid() {
		return false;
	}

	@Override
	public boolean handlesRefund() {
		return false;
	}

	@Override
	public boolean handlesPartialCapture() {
		return false;
	}

	@Override
	public boolean handlesMultipleShipment() {
		return false;
	}

	@Override
	public boolean handlesRecurringPayment() {
		return false;
	}

	@Override
	public boolean handlesSavedCustomerPayment() {
		return false;
	}

	@Override
	public boolean handlesMultiplePayments() {
		return false;
	}

	@Override
	public PaymentGatewayType getGatewayType() {
		return BraintreePaymentGatewayType.BRAINTREE_GATEWAY;
	}
}
