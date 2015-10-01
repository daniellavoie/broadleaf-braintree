package io.daniellavoie.blc.braintree.payment.service.gateway;

import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponsePrintService;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.payment.service.OrderPaymentService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BroadleafMockConfiguration {
	@Bean(name = "blOrderPaymentService")
	public OrderPaymentService orderPaymentService() {
		return Mockito.mock(OrderPaymentService.class);
	}

	@Bean(name = "blOrderService")
	public OrderService orderService() {
		return Mockito.mock(OrderService.class);
	}

	@Bean(name = "blPaymentGatewayWebResponsePrintService")
	public PaymentGatewayWebResponsePrintService paymentGatewayWebResponsePrintService() {
		return Mockito.mock(PaymentGatewayWebResponsePrintService.class);
	}
}
