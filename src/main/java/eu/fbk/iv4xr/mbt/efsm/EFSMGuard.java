package eu.fbk.iv4xr.mbt.efsm;

import java.io.Serializable;

public abstract class EFSMGuard implements Cloneable, Serializable {

	private static final long serialVersionUID = 986337555034767383L;

	public EFSMGuard() {
		// Constructor gol
	}

	@Override
	public EFSMGuard clone() {
		try {
			return (EFSMGuard) super.clone();
		} catch (CloneNotSupportedException e) {
			// Dacă clonarea eșuează, returnăm o instanță nouă
			return null;
		}
	}

	// Metoda abstractă pe care o implementăm în FifoQueueModel
	public abstract boolean guard(EFSMContext ctx);

	@Override
	public String toString() {
		return "EFSMGuard";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof EFSMGuard)) return false;
		return true;
	}
}