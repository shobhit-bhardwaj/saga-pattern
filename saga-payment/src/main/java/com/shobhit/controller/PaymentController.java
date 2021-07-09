package com.shobhit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shobhit.model.PaymentRequestBean;
import com.shobhit.model.PaymentResponseBean;
import com.shobhit.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@RequestMapping (value = "/debit", method = RequestMethod.POST)
	public PaymentResponseBean debit(@RequestBody PaymentRequestBean requestBean) {
		return paymentService.newPaymentTransaction(requestBean);
	}

	@RequestMapping (value = "/credit", method = RequestMethod.POST)
	public PaymentResponseBean credit(@RequestBody PaymentRequestBean requestBean) {
		return paymentService.cancelPaymentTransaction(requestBean);
	}
}