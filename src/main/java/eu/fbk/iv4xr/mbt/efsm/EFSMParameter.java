package eu.fbk.iv4xr.mbt.efsm;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;


public class EFSMParameter implements  Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6839254675772433933L;

	
	@Override
	public EFSMParameter clone() {
		return SerializationUtils.clone(this);
	}}

	



