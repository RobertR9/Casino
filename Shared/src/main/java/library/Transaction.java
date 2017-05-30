package library;

import java.math.BigDecimal;
import java.rmi.RemoteException;

public interface Transaction {

	public long getTransactionId() ;

	public String getTxHash() ;

	public BigDecimal getAmount() ;

	public long getDateEpoch() ;

	public long getConfirmations() ;
}
