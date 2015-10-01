/*
 * #%L
 * BroadleafCommerce Framework Web
 * %%
 * Copyright (C) 2009 - 2013 Broadleaf Commerce
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.cspinformatique.blc.braintree.payment.service.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.common.payment.PaymentTransactionType;
import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayRollbackService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.broadleafcommerce.core.order.service.OrderService;
import org.broadleafcommerce.core.payment.domain.OrderPayment;
import org.broadleafcommerce.core.payment.domain.PaymentTransaction;
import org.broadleafcommerce.core.payment.service.OrderPaymentService;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.ValidationError;
import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;

/**
 * @author Daniel Lavoie (daniellavoie)
 */
public class BraintreePaymentGatewayRollbackServiceImpl implements PaymentGatewayRollbackService {
	protected static final Log LOG = LogFactory.getLog(BraintreePaymentGatewayRollbackServiceImpl.class);

	private BraintreeGateway gateway;
	private OrderPaymentService orderPaymentService;
	private OrderService orderService;

	public BraintreePaymentGatewayRollbackServiceImpl(BraintreeGateway gateway, OrderPaymentService orderPaymentService,
			OrderService orderService) {
		this.gateway = gateway;
		this.orderPaymentService = orderPaymentService;
		this.orderService = orderService;
	}

	@Override
	public PaymentResponseDTO rollbackAuthorize(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
		return this.voidTransaction(transactionToBeRolledBack);
	}

	@Override
	public PaymentResponseDTO rollbackCapture(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
		throw new PaymentException("The Rollback Capture method is not supported for this module");
	}

	@Override
	public PaymentResponseDTO rollbackAuthorizeAndCapture(PaymentRequestDTO transactionToBeRolledBack)
			throws PaymentException {
		return this.voidTransaction(transactionToBeRolledBack);
	}

	@Override
	public PaymentResponseDTO rollbackRefund(PaymentRequestDTO transactionToBeRolledBack) throws PaymentException {
		throw new PaymentException("The Rollback Refund method is not supported for this module");
	}

	private PaymentResponseDTO voidTransaction(PaymentRequestDTO transactionToBeRolledBack) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Braintree Payment Gateway - Rolling back authorize transaction with amount: "
					+ transactionToBeRolledBack.getTransactionTotal());
		}

		boolean success = true;
		for (OrderPayment orderPayment : orderPaymentService.readPaymentsForOrder(
				orderService.findOrderById(Long.valueOf(transactionToBeRolledBack.getOrderId())))) {
			for (PaymentTransaction paymentTransaction : orderPayment.getTransactions()) {
				String gatewayTransactionId = paymentTransaction.getAdditionalFields().get("GATEWAY_TRANSACTION_ID");
				if (gatewayTransactionId != null) {
					Result<Transaction> result = gateway.transaction().voidTransaction(gatewayTransactionId);

					if (!result.isSuccess()) {
						for (ValidationError braintreeError : result.getErrors().getAllDeepValidationErrors()) {
							LOG.error(braintreeError.getMessage());
						}
						success = false;

					}
				}
			}
		}

		return new PaymentResponseDTO(PaymentType.CREDIT_CARD, BraintreePaymentGatewayType.BRAINTREE_GATEWAY)
				.rawResponse("rollback authorize - successful").successful(success)
				.paymentTransactionType(PaymentTransactionType.VOID)
				.amount(new Money(transactionToBeRolledBack.getTransactionTotal()));
	}

}
