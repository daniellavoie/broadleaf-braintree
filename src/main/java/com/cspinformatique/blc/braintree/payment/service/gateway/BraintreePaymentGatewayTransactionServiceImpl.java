package com.cspinformatique.blc.braintree.payment.service.gateway;

import java.math.BigDecimal;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransactionService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayConstants;
import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;

/**
 * This is an example implementation of a
 * {@link org.broadleafcommerce.common.payment.service.PaymentGatewayTransactionService}
 * . This handles the scenario where the implementation is PCI-Compliant and the
 * server directly handles the Credit Card PAN. If so, this service should make
 * a server to server call to charge the card against the configured gateway.
 *
 * In order to use load this demo service, you will need to component scan the
 * package "com.onoffmotosquads.sample".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Daniel Lavoie (daniellavoie)
 */
public class BraintreePaymentGatewayTransactionServiceImpl implements PaymentGatewayTransactionService {
	private BraintreeGateway gateway;

	public BraintreePaymentGatewayTransactionServiceImpl(BraintreeGateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public PaymentResponseDTO authorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
		return commonCreditCardProcessing(paymentRequestDTO, PaymentTransactionType.AUTHORIZE);
	}

	@Override
	public PaymentResponseDTO capture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
		PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
				BraintreePaymentGatewayType.BRAINTREE_GATEWAY);
		responseDTO.valid(true).paymentTransactionType(PaymentTransactionType.CAPTURE)
				.amount(new Money(paymentRequestDTO.getTransactionTotal())).rawResponse("Successful Capture")
				.successful(true);

		return responseDTO;
	}

	@Override
	public PaymentResponseDTO authorizeAndCapture(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
		return commonCreditCardProcessing(paymentRequestDTO, PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
	}

	@Override
	public PaymentResponseDTO reverseAuthorize(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
		throw new PaymentException("The Rollback authorize method is not supported for this module");
	}

	@Override
	public PaymentResponseDTO refund(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
		throw new PaymentException("The refund method is not supported for this module");
	}

	@Override
	public PaymentResponseDTO voidPayment(PaymentRequestDTO paymentRequestDTO) throws PaymentException {
		throw new PaymentException("The void method is not supported for this module");
	}

	/**
	 * Does minimal Credit Card Validation (luhn check and expiration date is
	 * after today). Mimics the Response of a real Payment Gateway.
	 *
	 * @param creditCardDTO
	 * @return
	 */
	protected PaymentResponseDTO commonCreditCardProcessing(PaymentRequestDTO requestDTO,
			PaymentTransactionType paymentTransactionType) {
		PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
				BraintreePaymentGatewayType.BRAINTREE_GATEWAY);

		responseDTO.valid(true).paymentTransactionType(paymentTransactionType);

		TransactionRequest braintreeRequest = new TransactionRequest()
				.amount(new BigDecimal(requestDTO.getTransactionTotal())).paymentMethodNonce(String.valueOf(
						requestDTO.getAdditionalFields().get(BraintreePaymentGatewayConstants.BRAINTREE_NONCE)));

		Result<Transaction> result = gateway.transaction().sale(braintreeRequest);

		if (!result.isSuccess()) {
			responseDTO.amount(new Money(0)).rawResponse(result.getMessage()).successful(false);
		} else {
			responseDTO.responseMap(BraintreePaymentGatewayConstants.GATEWAY_TRANSACTION_ID, result.getTarget().getId())
					.amount(new Money(requestDTO.getTransactionTotal())).rawResponse("Success!").successful(true);
		}

		return responseDTO;

	}
}
