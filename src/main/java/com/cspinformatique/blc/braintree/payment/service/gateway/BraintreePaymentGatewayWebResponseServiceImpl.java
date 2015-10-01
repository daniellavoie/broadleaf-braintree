package com.cspinformatique.blc.braintree.payment.service.gateway;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponsePrintService;
import org.broadleafcommerce.common.payment.service.PaymentGatewayWebResponseService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;

import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayConstants;
import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;

/**
 * This is an example implementation of a
 * {@link PaymentGatewayWebResponseService}. This will translate the Post
 * information back from
 * {@link com.cspinformatique.blc.braintreePaymentGateway.web.controller.BraintreePaymentGatewayProcessorController}
 * into a PaymentResponseDTO for processing in the Broadleaf System.
 *
 * Replace with a real Payment Gateway Integration like Braintree or PayPal
 * PayFlow.
 *
 * In order to use load this demo service, you will need to component scan the
 * package "com.onoffmotosquads.sample".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
public class BraintreePaymentGatewayWebResponseServiceImpl implements PaymentGatewayWebResponseService {
	private PaymentGatewayWebResponsePrintService webResponsePrintService;

	public BraintreePaymentGatewayWebResponseServiceImpl(
			PaymentGatewayWebResponsePrintService webResponsePrintService) {
		this.webResponsePrintService = webResponsePrintService;

	}

	@Override
	public PaymentResponseDTO translateWebResponse(HttpServletRequest request) throws PaymentException {
		PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
				BraintreePaymentGatewayType.BRAINTREE_GATEWAY)
						.rawResponse(webResponsePrintService.printRequest(request));

		Map<String, String[]> paramMap = request.getParameterMap();

		Money amount = Money.ZERO;
		if (paramMap.containsKey(BraintreePaymentGatewayConstants.TRANSACTION_AMT)) {
			String amt = paramMap.get(BraintreePaymentGatewayConstants.TRANSACTION_AMT)[0];
			amount = new Money(amt);
		}

		responseDTO.successful(true).completeCheckoutOnCallback(true).amount(amount)
				.paymentTransactionType(PaymentTransactionType.UNCONFIRMED)
				.orderId(paramMap.get(BraintreePaymentGatewayConstants.ORDER_ID)[0])
				.responseMap(BraintreePaymentGatewayConstants.BRAINTREE_NONCE,
						paramMap.get(BraintreePaymentGatewayConstants.BRAINTREE_NONCE)[0])
				.billTo().addressFirstName(paramMap.get(BraintreePaymentGatewayConstants.BILLING_FIRST_NAME)[0])
				.addressLastName(paramMap.get(BraintreePaymentGatewayConstants.BILLING_LAST_NAME)[0])
				.addressLine1(paramMap.get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE1)[0])
				.addressLine2(paramMap.get(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE2)[0])
				.addressCityLocality(paramMap.get(BraintreePaymentGatewayConstants.BILLING_CITY)[0])
				.addressStateRegion(paramMap.get(BraintreePaymentGatewayConstants.BILLING_STATE)[0])
				.addressPostalCode(paramMap.get(BraintreePaymentGatewayConstants.BILLING_ZIP)[0])
				.addressCountryCode(paramMap.get(BraintreePaymentGatewayConstants.BILLING_COUNTRY)[0]).done();

		return responseDTO;

	}

}
