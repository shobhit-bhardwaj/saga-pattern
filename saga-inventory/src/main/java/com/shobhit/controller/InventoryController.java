package com.shobhit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shobhit.model.InventoryRequestBean;
import com.shobhit.model.InventoryResponseBean;
import com.shobhit.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@RequestMapping (value = "/assign", method = RequestMethod.POST)
	public InventoryResponseBean assignInventory(@RequestBody InventoryRequestBean requestBean) {
		return inventoryService.newInventoryTransaction(requestBean);
	}

	@RequestMapping (value = "/revoke", method = RequestMethod.POST)
	public InventoryResponseBean revokeInventory(@RequestBody InventoryRequestBean requestBean) {
		return inventoryService.cancelInventoryTransaction(requestBean);
	}
}