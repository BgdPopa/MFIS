package eu.fbk.iv4xr.mbt.efsm;

import eu.fbk.iv4xr.mbt.fifo.FifoQueueModel;

public class EFSMFactory {
	private static EFSMFactory instance;
	private EFSM currentEfsm;

	private EFSMFactory() {}

	public static EFSMFactory getInstance() {
		if (instance == null) { instance = new EFSMFactory(); }
		return instance;
	}

	public EFSM getEFSM() {
		if (currentEfsm == null) { return getModel("fifo"); }
		return currentEfsm;
	}

	public EFSM getModel(String modelName) {
		if (modelName.equalsIgnoreCase("fifo")) {
			this.currentEfsm = new FifoQueueModel().buildModel();
			return currentEfsm;
		}
		return null;
	}

	public void setShortestPathsBetweenStates() {
		if (currentEfsm != null) { currentEfsm.setShortestPathsBetweenStates(); }
	}
}