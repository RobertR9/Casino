package library;

import java.math.BigDecimal;
import java.rmi.RemoteException;

public interface Transaction {

	public long getTransactionId() throws RemoteException;

	public String getTxHash() throws RemoteException;

	public BigDecimal getAmount() throws RemoteException;

	public long getDateEpoch() throws RemoteException;

	public long getConfirmations() throws RemoteException;
}
