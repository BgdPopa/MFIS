package eu.fbk.iv4xr.mbt.efsm;

import java.io.Serializable;


import org.apache.commons.lang3.SerializationUtils;


public abstract class EFSMOperation implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 503965196716794551L;
	
	private AssignSet operations;
	
	public EFSMOperation(Assign... assigns) {
		if (operations == null) {
			operations = new AssignSet();
		}
		for(Assign a : assigns ) {
			operations.put(a);
		}	
	}
	
	@Override
	public EFSMOperation clone() {
		
		return new EFSMOperation((Assign[]) operations.getHash().values().toArray()) {
			@Override
			public boolean execute(EFSMContext ctx) {
				return false;
			}
		};
	}
	
	public AssignSet getAssignments() {
		return this.operations;
	}
	

	@Override
	public String toString() {
		if (operations != null) {
			return operations.toDebugString();
		} else {
			return "NOP";
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof EFSMOperation) {
			EFSMOperation op = (EFSMOperation)o;
			if (op.getAssignments().equals(this.operations) ) {
				return true;
			}else {
				return false;
			}		
		}else {
			return false;
		}
	}

    public abstract boolean execute(EFSMContext ctx);
}
