package com.cspinformatique.blc.braintree.config;

import org.broadleafcommerce.common.payment.service.PaymentGatewayConfigurationService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayRollbackService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransactionService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponsePrintService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.payment.service.OrderPaymentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayConfiguration;
import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayConfigurationImpl;
import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayConfigurationServiceImpl;
import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayRollbackServiceImpl;
import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayTransactionServiceImpl;
import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayTransparentRedirectServiceImpl;
import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayWebResponseServiceImpl;

@Configuration
@EnableConfigurationProperties(BraintreePaymentGatewayConfigurationImpl.class)
public class BraintreeAutoConfiguration {
	@Bean(name = "dlBraintreeGateway")
	public BraintreeGateway braintreeGateway(BraintreePaymentGatewayConfiguration configuration) {
		return new BraintreeGateway(getEnvironment(configuration.getEnvironment()), configuration.getMerchantId(),
				configuration.getPublicKey(), configuration.getPrivateKey());
	}

	@Bean(name = "blBraintreePaymentGatewayConfigurationService")
	public PaymentGatewayConfigurationService paymentGatewayConfigurationService(
			BraintreePaymentGatewayConfiguration configuration,
			@Qualifier("blBraintreePaymentGatewayRollbackService") PaymentGatewayRollbackService rollbackService,
			@Qualifier("blBraintreePaymentGatewayTransactionService") PaymentGatewayTransactionService transactionService,
			@Qualifier("blBraintreePaymentGatewayWebResponseService") PaymentGatewayWebResponseService webResponseService,
			@Qualifier("blBraintreePaymentGatewayTransparentRedirectService") PaymentGatewayTransparentRedirectService transparentRedirectService) {
		return new BraintreePaymentGatewayConfigurationServiceImpl(configuration, rollbackService, transactionService,
				webResponseService, transparentRedirectService);
	}

	@Bean(name = "blBraintreePaymentGatewayRollbackService")
	public PaymentGatewayRollbackService paymentGatewayRollbackService(
			@Qualifier("dlBraintreeGateway") BraintreeGateway gateway,
			@Qualifier("blOrderPaymentService") OrderPaymentService orderPaymentService,
			@Qualifier("blOrderService") OrderService orderService) {
		return new BraintreePaymentGatewayRollbackServiceImpl(gateway, orderPaymentService, orderService);
	}

	@Bean(name = "blBraintreePaymentGatewayTransactionService")
	public PaymentGatewayTransactionService paymentGatewayTransactionService(
			@Qualifier("dlBraintreeGateway") BraintreeGateway gateway) {
		return new BraintreePaymentGatewayTransactionServiceImpl(gateway);
	}

	@Bean(name = "blBraintreePaymentGatewayTransparentRedirectService")
	public PaymentGatewayTransparentRedirectService paymentGatewayTransparentRedirectService(
			@Qualifier("dlBraintreeGateway") BraintreeGateway gateway,
			BraintreePaymentGatewayConfiguration gatewayConfiguration) {
		return new BraintreePaymentGatewayTransparentRedirectServiceImpl(gateway, gatewayConfiguration);
	}

	@Bean(name = "blBraintreePaymentGatewayWebResponseService")
	public PaymentGatewayWebResponseService paymentGatewayWebResponseService(
			@Qualifier("blPaymentGatewayWebResponsePrintService") PaymentGatewayWebResponsePrintService webResponsePrintService) {
		return new BraintreePaymentGatewayWebResponseServiceImpl(webResponsePrintService);
	}

	private Environment getEnvironment(String environmentName) {
		if (Environment.PRODUCTION.getEnvironmentName().equals(environmentName))
			return Environment.PRODUCTION;
		else if (Environment.SANDBOX.getEnvironmentName().equals(environmentName))
			return Environment.SANDBOX;
		else
			return Environment.DEVELOPMENT;
	}
}
