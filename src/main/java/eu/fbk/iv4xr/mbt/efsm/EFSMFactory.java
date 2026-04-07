package eu.fbk.iv4xr.mbt.efsm;
import eu.fbk.iv4xr.mbt.fifo.FifoQueueModel;

public class EFSMFactory {
	private static EFSMFactory instance;
	private EFSM currentEfsm;

	public static EFSMFactory getInstance() {
		if (instance == null) instance = new EFSMFactory();
		return instance;
	}

	public EFSM getEFSM() {
		return (currentEfsm != null) ? currentEfsm : getModel("fifo");
	}

	public EFSM getModel(String modelName) {
		if ("fifo".equalsIgnoreCase(modelName)) {
			this.currentEfsm = new FifoQueueModel().buildModel();
		}
		return currentEfsm;
	}
}