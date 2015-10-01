package io.daniellavoie.blc.braintree.payment.service.gateway;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cspinformatique.blc.braintree.payment.service.gateway.BraintreePaymentGatewayConfiguration;

@RunWith(SpringRunner.class)
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@SpringBootTest(classes = BraintreePaymentGatewayConfigurationImplTest.class, properties = {
		"braintree.environment=development", "braintree.merchant-id=mock-merchant-id",
		"braintree.public-key=mock-public-key", "braintree.private-key=mock-private-key" })
public class BraintreePaymentGatewayConfigurationImplTest {
	@Autowired
	@Qualifier("dlBraintreeGatewayConfig")
	private BraintreePaymentGatewayConfiguration gatewayConfiguration;

	@Test
	public void testConfiguration() {
		Assert.assertEquals("development", gatewayConfiguration.getEnvironment());
		Assert.assertEquals("mock-merchant-id", gatewayConfiguration.getMerchantId());
		Assert.assertEquals("mock-public-key", gatewayConfiguration.getPublicKey());
		Assert.assertEquals("mock-private-key", gatewayConfiguration.getPrivateKey());
	}
}
