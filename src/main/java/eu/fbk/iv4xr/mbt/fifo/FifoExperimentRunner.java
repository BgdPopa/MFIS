package eu.fbk.iv4xr.mbt.fifo;

import eu.fbk.iv4xr.mbt.efsm.EFSM;
import eu.fbk.iv4xr.mbt.efsm.EFSMState;
import eu.fbk.iv4xr.mbt.efsm.EFSMTransition;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FifoExperimentRunner {

    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static class GeneratedTestCase {
        List<String> transitions = new ArrayList<>();
        List<String> visitedStates = new ArrayList<>();
    }

    private static class RunStats {
        int seed;
        int generatedTests;
        int generatedSteps;
        int pushCount;
        int popCount;
        double transitionCoverage;
        double stateCoverage;
        Set<String> coveredTransitions = new HashSet<>();
        Set<String> coveredStates = new HashSet<>();
        List<GeneratedTestCase> suite = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        int runs = 5;
        int testsPerRun = 20;
        int maxStepsPerTest = 15;

        List<RunStats> allRuns = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            int seed = 100 + i;
            allRuns.add(executeRun(seed, testsPerRun, maxStepsPerTest));
        }

        Path outDir = Paths.get("results");
        Files.createDirectories(outDir);
        String timestamp = LocalDateTime.now().format(TS_FORMAT);
        writeCsv(outDir.resolve("fifo_experiment_" + timestamp + ".csv"), allRuns);
        writeSummary(outDir.resolve("fifo_experiment_summary_" + timestamp + ".md"), allRuns, runs, testsPerRun, maxStepsPerTest);

        System.out.println("Experiment finalizat.");
        System.out.println("Rulari: " + runs);
        System.out.println("Teste per rulare: " + testsPerRun);
        System.out.println("Pasi maximi per test: " + maxStepsPerTest);
        System.out.println("Rezultate scrise in folderul results/.");
    }

    private static RunStats executeRun(int seed, int testsPerRun, int maxStepsPerTest) {
        FifoQueueModel fifoQueueModel = new FifoQueueModel();
        EFSM model = fifoQueueModel.buildModel();
        Random random = new Random(seed);

        RunStats stats = new RunStats();
        stats.seed = seed;
        stats.coveredStates.add(model.getInitialConfiguration().getState().getId());
        int totalTransitions = model.getTransitons().size();
        int totalStates = model.getStates().size();

        for (int t = 0; t < testsPerRun; t++) {
            model.reset();
            GeneratedTestCase testCase = new GeneratedTestCase();
            testCase.visitedStates.add(model.getConfiguration().getState().getId());

            for (int step = 0; step < maxStepsPerTest; step++) {
                EFSMTransition chosen = pickFeasibleTransition(model, random);
                if (chosen == null) {
                    break;
                }

                model.transition(chosen);
                String transitionId = chosen.getId();
                String stateId = model.getConfiguration().getState().getId();

                testCase.transitions.add(transitionId);
                testCase.visitedStates.add(stateId);

                stats.coveredTransitions.add(transitionId);
                stats.coveredStates.add(stateId);
                stats.generatedSteps++;
                if (transitionId.startsWith("push")) {
                    stats.pushCount++;
                } else if (transitionId.startsWith("pop")) {
                    stats.popCount++;
                }
            }

            stats.generatedTests++;
            stats.suite.add(testCase);
        }

        stats.transitionCoverage = totalTransitions == 0 ? 0.0 : (100.0 * stats.coveredTransitions.size() / totalTransitions);
        stats.stateCoverage = totalStates == 0 ? 0.0 : (100.0 * stats.coveredStates.size() / totalStates);
        return stats;
    }

    private static EFSMTransition pickFeasibleTransition(EFSM model, Random random) {
        EFSMState currentState = model.getConfiguration().getState();
        List<EFSMTransition> feasible = new ArrayList<>();
        for (EFSMTransition transition : model.transitionsOutOf(currentState)) {
            if (transition.isFeasible(model.getConfiguration().getContext())) {
                feasible.add(transition);
            }
        }
        if (feasible.isEmpty()) {
            return null;
        }
        feasible.sort(Comparator.comparing(EFSMTransition::getId));
        return feasible.get(random.nextInt(feasible.size()));
    }

    private static void writeCsv(Path path, List<RunStats> runs) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("seed,tests,steps,push,pop,transition_coverage,state_coverage,covered_transitions,covered_states");
        for (RunStats r : runs) {
            lines.add(String.format(
                    "%d,%d,%d,%d,%d,%.2f,%.2f,%d,%d",
                    r.seed,
                    r.generatedTests,
                    r.generatedSteps,
                    r.pushCount,
                    r.popCount,
                    r.transitionCoverage,
                    r.stateCoverage,
                    r.coveredTransitions.size(),
                    r.coveredStates.size()
            ));
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private static void writeSummary(Path path, List<RunStats> runs, int runsCount, int testsPerRun, int maxSteps) throws IOException {
        double avgTransitionCoverage = runs.stream().mapToDouble(r -> r.transitionCoverage).average().orElse(0.0);
        double avgStateCoverage = runs.stream().mapToDouble(r -> r.stateCoverage).average().orElse(0.0);
        double avgSteps = runs.stream().mapToInt(r -> r.generatedSteps).average().orElse(0.0);
        double avgPush = runs.stream().mapToInt(r -> r.pushCount).average().orElse(0.0);
        double avgPop = runs.stream().mapToInt(r -> r.popCount).average().orElse(0.0);

        List<String> summary = new ArrayList<>();
        summary.add("# Analiza rezultate FIFO (EvoMBT-style)");
        summary.add("");
        summary.add("## Configuratie experiment");
        summary.add("- rulari: " + runsCount);
        summary.add("- teste per rulare: " + testsPerRun);
        summary.add("- pasi maximi per test: " + maxSteps);
        summary.add("- model: EFSM FIFO (stari EMPTY, PARTIAL, FULL)");
        summary.add("");
        summary.add("## Rezultate agregate");
        summary.add(String.format("- acoperire medie tranzitii: %.2f%%", avgTransitionCoverage));
        summary.add(String.format("- acoperire medie stari: %.2f%%", avgStateCoverage));
        summary.add(String.format("- pasi medii executati per rulare: %.2f", avgSteps));
        summary.add(String.format("- tranzitii medii PUSH per rulare: %.2f", avgPush));
        summary.add(String.format("- tranzitii medii POP per rulare: %.2f", avgPop));
        summary.add("");
        summary.add("## Concluzie");
        summary.add("Modelul FIFO este integrat in framework-ul curent si produce automat suite de teste abstracte.");
        summary.add("Rezultatele includ acoperire pe stari/tranzitii si distributia actiunilor PUSH/POP.");

        Files.write(path, summary, StandardCharsets.UTF_8);
    }
}
