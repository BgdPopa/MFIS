package eu.fbk.iv4xr.mbt.fifo;

import eu.fbk.iv4xr.mbt.efsm.EFSM;
import eu.fbk.iv4xr.mbt.efsm.EFSMBuilder;
import eu.fbk.iv4xr.mbt.efsm.EFSMContext;
import eu.fbk.iv4xr.mbt.efsm.EFSMTransition;
import eu.fbk.iv4xr.mbt.efsm.EFSMState;
import eu.fbk.iv4xr.mbt.efsm.EFSMParameterGenerator;
import eu.fbk.iv4xr.mbt.efsm.EFSMGuard;
import eu.fbk.iv4xr.mbt.efsm.EFSMOperation;

public class FifoQueueModel {

    // 1. Definim Stările
    public enum State { EMPTY, PARTIAL, FULL }

    // 2. Definim Contextul (Memoria)
    public static class FifoContext extends EFSMContext {
        public int currentSize = 0;
        public final int MAX_CAPACITY = 5;
    }

    // 3. Metoda care construiește și returnează modelul EFSM
    public EFSM buildModel() {
        FifoContext context = new FifoContext();
        EFSMBuilder builder = new EFSMBuilder(EFSM.class);

        // a) Definim tranziția PUSH (Adaugă element)
        EFSMTransition pushTransition = new EFSMTransition();
        pushTransition.setId("push");

        pushTransition.setGuard(new EFSMGuard() {
            @Override
            public boolean guard(EFSMContext ctx) {
                FifoContext c = (FifoContext) ctx;
                return c.currentSize < c.MAX_CAPACITY;
            }
        });

        pushTransition.setOp(new EFSMOperation() {
            @Override
            public boolean execute(EFSMContext ctx) {
                FifoContext c = (FifoContext) ctx;
                c.currentSize++;
                return true;
            }
        });

        // b) Definim tranziția POP (Scoate element)
        EFSMTransition popTransition = new EFSMTransition();
        popTransition.setId("pop");

        popTransition.setGuard(new EFSMGuard() {
            @Override
            public boolean guard(EFSMContext ctx) {
                FifoContext c = (FifoContext) ctx;
                return c.currentSize > 0;
            }
        });

        popTransition.setOp(new EFSMOperation() {
            @Override
            public boolean execute(EFSMContext ctx) {
                FifoContext c = (FifoContext) ctx;
                c.currentSize--;
                return true;
            }
        });

        // c) Construim Graful (legăm stările cu tranzițiile)
        builder
                .withTransition(new EFSMState(State.EMPTY.name()), new EFSMState(State.PARTIAL.name()), pushTransition)
                .withTransition(new EFSMState(State.PARTIAL.name()), new EFSMState(State.PARTIAL.name()), pushTransition)
                .withTransition(new EFSMState(State.PARTIAL.name()), new EFSMState(State.FULL.name()), pushTransition)
                .withTransition(new EFSMState(State.PARTIAL.name()), new EFSMState(State.PARTIAL.name()), popTransition)
                .withTransition(new EFSMState(State.PARTIAL.name()), new EFSMState(State.EMPTY.name()), popTransition)
                .withTransition(new EFSMState(State.FULL.name()), new EFSMState(State.PARTIAL.name()), popTransition);

        // FIX: Am pasat explicit un null forțat la tipul cerut de EvoMBT
        EFSM model = builder.build(new EFSMState(State.EMPTY.name()), context, (EFSMParameterGenerator) null);
        return model;
    }
}