package eu.fbk.iv4xr.mbt.fifo;

import eu.fbk.iv4xr.mbt.efsm.EFSM;

public class FifoRunner {
    public static void main(String[] args) {
        System.out.println("--- PORNIM TESTAREA EVO-MBT ---");
        System.out.println("Inițializăm modelul FIFO...");

        // Chemăm clasa creată de tine
        FifoQueueModel modelBuilder = new FifoQueueModel();
        EFSM model = modelBuilder.buildModel();

        // Verificăm dacă s-a construit corect cerând starea inițială
        System.out.println("Modelul a fost creat cu succes!");
        System.out.println("Starea inițială a cozii este: " + model.getInitialConfiguration().getState().getId());
        System.out.println("-------------------------------");
    }
}