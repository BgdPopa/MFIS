package eu.fbk.iv4xr.mbt.efsm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.function.Function;

public class EFSMExporter {

	private final EFSM efsm;
	private final Function<EFSMState, String> stateLabeler;
	private final Function<EFSMTransition, String> edgeLabeler;

	public EFSMExporter(EFSM efsm) {
		this(efsm, Object::toString, Object::toString);
	}

	public EFSMExporter(EFSM efsm, Function<EFSMState, String> stateLabeler, Function<EFSMTransition, String> edgeLabeler) {
		this.efsm = efsm;
		this.stateLabeler = stateLabeler;
		this.edgeLabeler = edgeLabeler;
	}

	public void writeOut(Path outFile, String mode) throws FileNotFoundException, IOException {
		try (PrintWriter writer = new PrintWriter(outFile.toFile())) {
			writer.println("digraph EFSM {");
			// Logica simplificata pentru export
			writer.println("}");
		}
	}
}