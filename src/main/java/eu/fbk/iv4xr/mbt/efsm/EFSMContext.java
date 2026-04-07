package eu.fbk.iv4xr.mbt.efsm;

import java.io.Serializable;

/**
 * Versiune simplificata a EFSMContext pentru a elimina dependentele
 * de pachetele sterse (exp, cps, labrecruits).
 */
public class EFSMContext implements Cloneable, Serializable {

	private static final long serialVersionUID = -7951703032359100975L;

	public EFSMContext() {
		// Constructor gol pentru utilizare generica
	}

	@Override
	public EFSMContext clone() {
		try {
			return (EFSMContext) super.clone();
		} catch (CloneNotSupportedException e) {
			return new EFSMContext();
		}
	}

	// Metoda ceruta de motorul de executie EvoMBT
	public EFSMContext getNewCopy() {
		return this.clone();
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof EFSMContext)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "EFSMContext{}";
	}
}