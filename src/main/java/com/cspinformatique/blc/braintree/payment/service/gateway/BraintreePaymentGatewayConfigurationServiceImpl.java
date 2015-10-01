package com.cspinformatique.blc.braintree.payment.service.gateway;

import org.broadleafcommerce.common.payment.service.PaymentGatewayClientTokenService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfiguration;
import org.broadleafcommerce.common.payment.service.PaymentGatewayConfigurationService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayCreditCardService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayCustomerService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayFraudService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayHostedService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayReportingService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayRollbackService;
import org.broadleafcommerce.common.payment.service.PaymentGatewaySubscriptionService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransactionConfirmationService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransactionService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.web.payment.expression.PaymentGatewayFieldExtensionHandler;
import org.broadleafcommerce.common.web.payment.processor.CreditCardTypesExtensionHandler;
import org.broadleafcommerce.common.web.payment.processor.TRCreditCardExtensionHandler;

public class BraintreePaymentGatewayConfigurationServiceImpl implements PaymentGatewayConfigurationService {
	private BraintreePaymentGatewayConfiguration configuration;
	private PaymentGatewayRollbackService rollbackService;
	private PaymentGatewayTransactionService transactionService;
	private PaymentGatewayWebResponseService webResponseService;
	private PaymentGatewayTransparentRedirectService transparentRedirectService;

	public BraintreePaymentGatewayConfigurationServiceImpl(BraintreePaymentGatewayConfiguration configuration,
			PaymentGatewayRollbackService rollbackService, PaymentGatewayTransactionService transactionService,
			PaymentGatewayWebResponseService webResponseService,
			PaymentGatewayTransparentRedirectService transparentRedirectService) {
		this.configuration = configuration;
		this.rollbackService = rollbackService;
		this.transactionService = transactionService;
		this.webResponseService = webResponseService;
		this.transparentRedirectService = transparentRedirectService;
	}

	public PaymentGatewayConfiguration getConfiguration() {
		return configuration;
	}

	public PaymentGatewayTransactionConfirmationService getTransactionConfirmationService() {
		return null;
	}

	public PaymentGatewayReportingService getReportingService() {
		return null;
	}

	public PaymentGatewayCreditCardService getCreditCardService() {
		return null;
	}

	public PaymentGatewayCustomerService getCustomerService() {
		return null;
	}

	public PaymentGatewaySubscriptionService getSubscriptionService() {
		return null;
	}

	public PaymentGatewayFraudService getFraudService() {
		return null;
	}

	public PaymentGatewayHostedService getHostedService() {
		return null;
	}

	public PaymentGatewayRollbackService getRollbackService() {
		return rollbackService;
	}

	public PaymentGatewayWebResponseService getWebResponseService() {
		return webResponseService;
	}

	public PaymentGatewayTransparentRedirectService getTransparentRedirectService() {
		return transparentRedirectService;
	}

	public PaymentGatewayTransactionService getTransactionService() {
		return transactionService;
	}

	public TRCreditCardExtensionHandler getCreditCardExtensionHandler() {
		return null;
	}

	public PaymentGatewayFieldExtensionHandler getFieldExtensionHandler() {
		return null;
	}

	public CreditCardTypesExtensionHandler getCreditCardTypesExtensionHandler() {
		return null;
	}

	@Override
	public PaymentGatewayClientTokenService getClientTokenService() {
		return null;
	}
}
