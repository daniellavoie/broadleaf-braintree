package com.cspinformatique.blc.braintree.payment.service.gateway;

import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;

public interface BraintreePaymentGatewayConfiguration extends PaymentGatewayConfiguration {
	String getEnvironment();
	
	String getMerchantId();

	String getPublicKey();

	String getPrivateKey();

	String getTransparentRedirectUrl();

	String getTransparentRedirectReturnUrl();
}
