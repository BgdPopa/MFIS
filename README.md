# EvoMBT FIFO (MFIS)

Acest proiect este o variantă redusă pentru tema MFIS:

- păstrează doar nucleul EFSM minim necesar pentru modelare;
- elimină componentele legacy legate de scenarii externe;
- include modelul `FifoQueueModel` și runner-ul `FifoRunner`.

## Cerințe

- Java 11+
- Maven 3+

## Build

```bash
mvn clean compile
```

## Rulare demo FIFO

```bash
mvn -q -DskipTests exec:java -Dexec.mainClass=eu.fbk.iv4xr.mbt.fifo.FifoRunner
```

## Generare suita de teste + analiza rezultate

```bash
mvn -q -DskipTests exec:java -Dexec.mainClass=eu.fbk.iv4xr.mbt.fifo.FifoExperimentRunner
```

Rezultatele sunt scrise automat in folderul `results/`:
- un fisier CSV cu metrici pe fiecare rulare;
- un fisier Markdown cu analiza agregata.
