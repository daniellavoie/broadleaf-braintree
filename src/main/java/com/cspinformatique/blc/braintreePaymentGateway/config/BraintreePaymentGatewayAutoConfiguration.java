package com.cspinformatique.blc.braintreePaymentGateway.config;

import org.broadleafcommerce.common.extensibility.context.merge.LateStageMergeBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cspinformatique.blc.braintreePaymentGateway.web.controller.BraintreePaymentGatewayController;
import com.cspinformatique.blc.braintreePaymentGateway.web.processor.BraintreePaymentGatewayTRExtensionHandler;

@Configuration
@Import({ BraintreePaymentGatewayController.class, BraintreePaymentGatewayTRExtensionHandler.class })
public class BraintreePaymentGatewayAutoConfiguration {
	@Bean
	public LateStageMergeBeanPostProcessor braintreeMergeBeanPostProcessor() {
		LateStageMergeBeanPostProcessor braintreeMergeBeanPostProcessor = new LateStageMergeBeanPostProcessor();

		braintreeMergeBeanPostProcessor.setSourceRef("blBraintreePaymentGatewayConfigurationService");
		braintreeMergeBeanPostProcessor.setTargetRef("blPaymentGatewayConfigurationServices");

		return braintreeMergeBeanPostProcessor;
	}
}
