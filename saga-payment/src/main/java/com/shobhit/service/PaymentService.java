package com.shobhit.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shobhit.constants.PaymentStatus;
import com.shobhit.entity.User;
import com.shobhit.entity.UserTransaction;
import com.shobhit.model.PaymentRequestBean;
import com.shobhit.model.PaymentResponseBean;
import com.shobhit.repository.UserRepository;
import com.shobhit.repository.UserTransactionRepository;

@Service
public class PaymentService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserTransactionRepository userTransactionRepository;

	@PostConstruct
	public void loadUserData() {
		List<User> users = Arrays.asList(
				new User("Ramesh", 5000),
				new User("Vinay", 6000),
				new User("Kapil", 7000),
				new User("Ajay", 8000),
				new User("Shreekant", 9000));
		userRepository.saveAll(users);
	}

	@Transactional
	public PaymentResponseBean newPaymentTransaction(PaymentRequestBean requestBean) {
		List<UserTransaction> transactions = userTransactionRepository.findByOrderId(requestBean.getOrderId());

		if(transactions == null || transactions.size() == 0) {
			User user = userRepository.findById(requestBean.getUserId()).orElse(null);

			if(user != null) {
				double balance = user.getBalance();

				if(balance < requestBean.getAmount()) {
					UserTransaction userTransaction = new UserTransaction(requestBean.getOrderId(), requestBean.getUserId(), requestBean.getAmount(), PaymentStatus.Payment_Rejected);
					userTransaction = userTransactionRepository.save(userTransaction);

					return new PaymentResponseBean(requestBean.getOrderId(), userTransaction.getTransactionId(), PaymentStatus.Payment_Rejected);
				} else {
					user.setBalance(user.getBalance() - requestBean.getAmount());
					userRepository.save(user);

					UserTransaction userTransaction = new UserTransaction(requestBean.getOrderId(), requestBean.getUserId(), requestBean.getAmount(), PaymentStatus.Payment_Received);
					userTransaction = userTransactionRepository.save(userTransaction);

					return new PaymentResponseBean(requestBean.getOrderId(), userTransaction.getTransactionId(), PaymentStatus.Payment_Received);
				}
			} else {
				return new PaymentResponseBean(requestBean.getOrderId(), 0, PaymentStatus.Unknown_Transaction);
			}
		} else {
			return new PaymentResponseBean(requestBean.getOrderId(), transactions.get(0).getTransactionId(), PaymentStatus.Transaction_Already_Processed);
		}
	}

	@Transactional
	public PaymentResponseBean cancelPaymentTransaction(PaymentRequestBean requestBean) {
		List<UserTransaction> transactions = userTransactionRepository.findByOrderId(requestBean.getOrderId());

		if(transactions != null && transactions.size() == 1 && PaymentStatus.Payment_Received.name().equalsIgnoreCase(transactions.get(0).getPaymentStatus())) {
			User user = userRepository.findById(requestBean.getUserId()).orElse(null);

			if(user != null) {
				user.setBalance(user.getBalance() + requestBean.getAmount());
				userRepository.save(user);

				UserTransaction userTransaction = new UserTransaction(requestBean.getOrderId(), requestBean.getUserId(), requestBean.getAmount(), PaymentStatus.Payment_Settled);
				userTransaction = userTransactionRepository.save(userTransaction);

				return new PaymentResponseBean(requestBean.getOrderId(), userTransaction.getTransactionId(), PaymentStatus.Payment_Settled);
			} else {
				return new PaymentResponseBean(requestBean.getOrderId(), 0, PaymentStatus.Unknown_Transaction);
			}
		} else {
			return new PaymentResponseBean(requestBean.getOrderId(), 0, PaymentStatus.Transaction_Already_Processed);
		}
	}
}