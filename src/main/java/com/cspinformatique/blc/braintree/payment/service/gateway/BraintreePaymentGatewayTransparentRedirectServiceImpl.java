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

import org.broadleafcommerce.common.payment.PaymentType;
import org.broadleafcommerce.common.payment.dto.AddressDTO;
import org.broadleafcommerce.common.payment.dto.PaymentRequestDTO;
import org.broadleafcommerce.common.payment.dto.PaymentResponseDTO;
import org.broadleafcommerce.common.payment.service.PaymentGatewayTransparentRedirectService;
import org.broadleafcommerce.common.vendor.service.exception.PaymentException;
import org.springframework.util.Assert;

import com.braintreegateway.BraintreeGateway;
import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayConstants;
import com.cspinformatique.blc.braintreePaymentGateway.service.payment.BraintreePaymentGatewayType;

/**
 * This is an example implementation of a
 * {@link PaymentGatewayTransparentRedirectService}. This is just a sample that
 * mimics what hidden fields a real payment gateway implementation might put on
 * your transparent redirect credit card form on your checkout page. Replace
 * with a real Payment Gateway Integration like Braintree or PayPal PayFlow.
 *
 * In order to use load this demo service, you will need to component scan the
 * package "com.onoffmotosquads.sample".
 *
 * This should NOT be used in production, and is meant solely for demonstration
 * purposes only.
 *
 * @author Elbert Bautista (elbertbautista)
 */
public class BraintreePaymentGatewayTransparentRedirectServiceImpl implements PaymentGatewayTransparentRedirectService {
	private BraintreeGateway gateway;
	private BraintreePaymentGatewayConfiguration gatewayConfiguration;

	public BraintreePaymentGatewayTransparentRedirectServiceImpl(BraintreeGateway gateway,
			BraintreePaymentGatewayConfiguration gatewayConfiguration) {
		this.gateway = gateway;
		this.gatewayConfiguration = gatewayConfiguration;

	}

	@Override
	public PaymentResponseDTO createAuthorizeAndCaptureForm(PaymentRequestDTO requestDTO) throws PaymentException {
		return createCommonTRFields(requestDTO);
	}

	@Override
	public PaymentResponseDTO createAuthorizeForm(PaymentRequestDTO requestDTO) throws PaymentException {
		return createCommonTRFields(requestDTO);
	}

	protected PaymentResponseDTO createCommonTRFields(PaymentRequestDTO requestDTO) {
		Assert.isTrue(requestDTO.getTransactionTotal() != null,
				"The Transaction Total on the Payment Request DTO must not be null");
		Assert.isTrue(requestDTO.getOrderId() != null, "The Order ID on the Payment Request DTO must not be null");

		// Put The shipping, billing, and transaction amount fields as hidden
		// fields on the form
		// In a real implementation, the gateway will probably provide some API
		// to tokenize this information
		// which you can then put on your form as a secure token. For this
		// sample,
		// we will just place them as plain-text hidden fields on the form
		PaymentResponseDTO responseDTO = new PaymentResponseDTO(PaymentType.CREDIT_CARD,
				BraintreePaymentGatewayType.BRAINTREE_GATEWAY)
						.responseMap(BraintreePaymentGatewayConstants.ORDER_ID, requestDTO.getOrderId())
						.responseMap(BraintreePaymentGatewayConstants.TRANSACTION_AMT, requestDTO.getTransactionTotal())
						.responseMap(BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL,
								gatewayConfiguration.getTransparentRedirectUrl());

		AddressDTO<PaymentRequestDTO> billTo = requestDTO.getBillTo();
		if (billTo != null) {
			responseDTO.responseMap(BraintreePaymentGatewayConstants.BILLING_FIRST_NAME, billTo.getAddressFirstName())
					.responseMap(BraintreePaymentGatewayConstants.BILLING_LAST_NAME, billTo.getAddressLastName())
					.responseMap(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE1, billTo.getAddressLine1())
					.responseMap(BraintreePaymentGatewayConstants.BILLING_ADDRESS_LINE2, billTo.getAddressLine2())
					.responseMap(BraintreePaymentGatewayConstants.BILLING_CITY, billTo.getAddressCityLocality())
					.responseMap(BraintreePaymentGatewayConstants.BILLING_STATE, billTo.getAddressStateRegion())
					.responseMap(BraintreePaymentGatewayConstants.BILLING_ZIP, billTo.getAddressPostalCode())
					.responseMap(BraintreePaymentGatewayConstants.BILLING_COUNTRY, billTo.getAddressCountryCode());
		}

		AddressDTO<PaymentRequestDTO> shipTo = requestDTO.getShipTo();
		if (shipTo != null) {
			responseDTO.responseMap(BraintreePaymentGatewayConstants.SHIPPING_FIRST_NAME, shipTo.getAddressFirstName())
					.responseMap(BraintreePaymentGatewayConstants.SHIPPING_LAST_NAME, shipTo.getAddressLastName())
					.responseMap(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE1, shipTo.getAddressLine1())
					.responseMap(BraintreePaymentGatewayConstants.SHIPPING_ADDRESS_LINE2, shipTo.getAddressLine2())
					.responseMap(BraintreePaymentGatewayConstants.SHIPPING_CITY, shipTo.getAddressCityLocality())
					.responseMap(BraintreePaymentGatewayConstants.SHIPPING_STATE, shipTo.getAddressStateRegion())
					.responseMap(BraintreePaymentGatewayConstants.SHIPPING_ZIP, shipTo.getAddressPostalCode())
					.responseMap(BraintreePaymentGatewayConstants.SHIPPING_COUNTRY, shipTo.getAddressCountryCode());
		}

		// Calls braintree API to retreive the client token.
		responseDTO.responseMap(BraintreePaymentGatewayConstants.BRAINTREE_CLIENT_TOKEN,
				gateway.clientToken().generate());

		return responseDTO;

	}

	@Override
	public PaymentResponseDTO createCustomerPaymentTokenForm(PaymentRequestDTO requestDTO) throws PaymentException {
		return createCommonTRFields(requestDTO);
	}

	@Override
	public String getCreateCustomerPaymentTokenCancelURLFieldKey(PaymentResponseDTO requestDTO) {
		return BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
	}

	@Override
	public String getCreateCustomerPaymentTokenReturnURLFieldKey(PaymentResponseDTO requestDTO) {
		return BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
	}

	@Override
	public String getUpdateCustomerPaymentTokenCancelURLFieldKey(PaymentResponseDTO requestDTO) {
		return BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
	}

	@Override
	public String getUpdateCustomerPaymentTokenReturnURLFieldKey(PaymentResponseDTO requestDTO) {
		return BraintreePaymentGatewayConstants.TRANSPARENT_REDIRECT_URL;
	}

	@Override
	public PaymentResponseDTO updateCustomerPaymentTokenForm(PaymentRequestDTO requestDTO) throws PaymentException {
		return createCommonTRFields(requestDTO);
	}

}
