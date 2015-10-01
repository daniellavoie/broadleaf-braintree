package com.cspinformatique.blc.braintreePaymentGateway.service.payment;

import org.broadleafcommerce.common.payment.PaymentGatewayType;

public class BraintreePaymentGatewayType extends PaymentGatewayType {
	private static final long serialVersionUID = 1L;

	public static final PaymentGatewayType BRAINTREE_GATEWAY = new PaymentGatewayType("BRAINTREE_GATEWAY",
			"Braintree Payment Gateway Implementation");
	public static final PaymentGatewayType BRAINTREE_HOSTED_GATEWAY = new PaymentGatewayType("BRAINTREE_HOSTED_GATEWAY",
			"Braintree Hosted Payment Gateway Implementation");
}
