package com.shobhit.process;

import org.springframework.web.reactive.function.client.WebClient;

import com.shobhit.model.InventoryRequestBean;
import com.shobhit.model.InventoryResponseBean;

import reactor.core.publisher.Mono;

public class InventoryWorkFlowStep implements WorkFlowStep<InventoryResponseBean> {
	private InventoryRequestBean requestBean;
	private WebClient webClient;

	public InventoryWorkFlowStep(InventoryRequestBean requestBean, WebClient webClient) {
		this.requestBean = requestBean;
		this.webClient = webClient;
	}

	@Override
	public InventoryResponseBean process() {
		InventoryResponseBean responseBean = webClient.post()
				.uri("/inventory/assign")
				.body(Mono.just(requestBean), InventoryRequestBean.class)
				.retrieve()
				.bodyToMono(InventoryResponseBean.class)
				.block();

		return responseBean;
	}

	@Override
	public InventoryResponseBean revert() {
		InventoryResponseBean responseBean = webClient.post()
				.uri("/inventory/revoke")
				.body(Mono.just(requestBean), InventoryRequestBean.class)
				.retrieve()
				.bodyToMono(InventoryResponseBean.class)
				.block();

		return responseBean;
	}
}