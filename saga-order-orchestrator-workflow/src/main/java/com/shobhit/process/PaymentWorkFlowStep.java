package com.shobhit.process;

import org.springframework.web.reactive.function.client.WebClient;

import com.shobhit.model.PaymentRequestBean;
import com.shobhit.model.PaymentResponseBean;

import reactor.core.publisher.Mono;

public class PaymentWorkFlowStep implements WorkFlowStep<PaymentResponseBean> {
	private PaymentRequestBean requestBean;
	private WebClient webClient;

	public PaymentWorkFlowStep(PaymentRequestBean requestBean, WebClient webClient) {
		this.requestBean = requestBean;
		this.webClient = webClient;
	}

	@Override
	public PaymentResponseBean process() {
		PaymentResponseBean responseBean = webClient.post()
				.uri("/payment/debit")
				.body(Mono.just(requestBean), PaymentRequestBean.class)
				.retrieve()
				.bodyToMono(PaymentResponseBean.class)
				.block();

		return responseBean;
	}

	@Override
	public PaymentResponseBean revert() {
		PaymentResponseBean responseBean = webClient.post()
				.uri("/payment/credit")
				.body(Mono.just(requestBean), PaymentRequestBean.class)
				.retrieve()
				.bodyToMono(PaymentResponseBean.class)
				.block();

		return responseBean;
	}
}