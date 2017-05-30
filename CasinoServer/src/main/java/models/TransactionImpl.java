package models;

import library.Transaction;

import java.math.BigDecimal;


public class TransactionImpl implements Transaction {

	private static final long serialVersionUID = 1L;

	private long transactionId;

	private AuthPlayerImpl player;

	private String hash;

	private BigDecimal amount;

	private long confirmations;

	private long dateEpoch;

	public TransactionImpl()  {
		super();
	}

	public TransactionImpl(String hash, BigDecimal amount, long confirmations, long dateEpoch, AuthPlayerImpl player) {
		this.hash = hash;
		this.amount = amount;
		this.confirmations = confirmations;
		this.dateEpoch = dateEpoch;
		this.player = player;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public String getTxHash() {
		return hash;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public long getDateEpoch() {
		return dateEpoch;
	}

	public long getConfirmations() {
		return confirmations;
	}

}
