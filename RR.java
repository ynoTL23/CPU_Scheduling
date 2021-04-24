import java.util.ArrayList;

public class RR {
  private int cpuTime, baseTimeQuantum;
  private boolean isIdle;
  private ArrayList<Process> rrProcesses, readyQueue, finishedProcesses;

  public RR(ArrayList<Process> processes, int timeQuantum) {
    this.setCPUTime(0);
    this.setTimeQuantum(timeQuantum);
    this.rrProcesses = new ArrayList<>();
    this.readyQueue = new ArrayList<Process>();
    this.finishedProcesses = new ArrayList<Process>();

    /**
      when passing objects, their references are passed
      instead of their values
      we have to clone/deep copy them to avoid mutating
      the original
    */
    for (Process p : processes) {
      this.rrProcesses.add(p.clone());
    }
  }

  public void runRR() {
    System.out.println("\n+" + "-".repeat(34) + "+");
    System.out.println("|      Round Robin Scheduling      |");
    System.out.println("+" + "-".repeat(34) + "+");

    simulateRR();
  }

  public void simulateRR() {
    setIsIdle(true); // CPU is idle, waiting for jobs

    // run until all processes have been ran and no processes remain in the ready queue
    runCPUCycle(getTimeQuantum());
    printSummary(finishedProcesses);
  }

  private void runCPUCycle(int timeQuantum) {
    int remainingTimeQuantum = timeQuantum;

    // check if cpu time allows for more processes to be added into ready queue
    if (!this.rrProcesses.isEmpty() && this.rrProcesses.get(0).getArrivalTime() <= getCPUTime()) {
      this.readyQueue.add(this.rrProcesses.remove(0));
    }

    // check if readyQueue has processes to run
    // else, processes are done
    if (!this.readyQueue.isEmpty()) {
      Process currentProcess = this.readyQueue.get(0);
      if (currentProcess.getArrivalTime() < getCPUTime()) setIsIdle(false);

      // check if CPU currently has a job
      if(!isIdle()) {
        // check if the current process isn't constrained by time quantum and has burst left
        if (currentProcess.getRemainingBurstTime() != 0 && remainingTimeQuantum != 0) {
          // if this is our first time running the process, set the start time
          if (currentProcess.getStartTime() == -1) currentProcess.setStartTime(getCPUTime() - 1);

          // run one CPU cycle on this process
          System.out.printf("[CPU Time: %02d] CPU is working on PID %d\n", getCPUTime(), currentProcess.getPID());
          currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
          this.readyQueue.set(0, currentProcess);
          remainingTimeQuantum -= 1;
        }

        // process can't continue because of time quantum
        if (remainingTimeQuantum == 0 && currentProcess.getRemainingBurstTime() > 0) {
          Process tempProcess = this.readyQueue.remove(0);
          this.readyQueue.add(tempProcess);
        } else if (currentProcess.getRemainingBurstTime() == 0) {
          // process finished running completely because no more burst time remains
          currentProcess.setCompletionTime(getCPUTime());
          currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
          currentProcess.setWaitTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime() - currentProcess.getBurstTime());
          currentProcess.setResponseTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
          Process tempProcess = this.readyQueue.remove(0);
          this.finishedProcesses.add(tempProcess);
          setIsIdle(true);
        }
      } else {
        System.out.printf("[CPU Time: %02d] CPU is currently idle...\n", getCPUTime());
      }

      setCPUTime(getCPUTime() + 1);
      if (remainingTimeQuantum >= 1 && !isIdle()) runCPUCycle(remainingTimeQuantum);
      else runCPUCycle(getTimeQuantum());
    } else {
      // nothing in readyQueue
      System.out.printf("[CPU Time: %02d] CPU is currently idle...\n", getCPUTime());
      setCPUTime(getCPUTime() + 1);
    }
  }

  private int getCPUTime() {
    return this.cpuTime;
  }

  private int getTimeQuantum() {
    return this.baseTimeQuantum;
  }

  private double getAvgTurnaroundTime(ArrayList<Process> jobs) {
    int totalTurnaroundTime = 0;

    for (Process process : jobs) {
      totalTurnaroundTime += process.getTurnaroundTime();
    }

    return (double) totalTurnaroundTime / jobs.size();
  }

  private double getAvgWaitTime(ArrayList<Process> jobs) {
    int totalWaitTime = 0;

    for (Process process : jobs) {
      totalWaitTime += process.getWaitTime();
    }

    return (double) totalWaitTime / jobs.size();
  }

  private double getAvgResponseTime(ArrayList<Process> jobs) {
    int totalResponseTime = 0;

    for (Process process : jobs) {
      totalResponseTime += process.getResponseTime();
    }
    return (double) totalResponseTime / jobs.size();
  }

  private void printSummary(ArrayList<Process> jobs) {
    // sort processes first to make the output look nice
    // RR may have shuffled the processes around
    jobs.sort((p1, p2) -> p1.getArrivalTime() - p2.getArrivalTime());

    System.out.println("\n>>> Round Robin (RR) Scheduling Summary <<<");
    System.out.printf("+%s+\n", "-".repeat(98));
    System.out.printf("|%2sProcess ID%2s|%2sArrival Time%2s|%2sBurst Time%2s|%2sTurnaround Time%2s|%2sWait Time%2s|%2sResponse Time%2s|\n",
      "", "", "", "", "", "", "", "", "", "", "", "");
    System.out.printf("+%s+\n", "-".repeat(98));

    for (Process process : jobs) {
      System.out.printf("|%6s%02d%6s|%7s%02d%7s|%6s%02d%6s|%9s%02d%8s|%6s%02d%5s|%8s%02d%7s|\n",
        "", process.getPID(), "",
        "", process.getArrivalTime(), "",
        "", process.getBurstTime(), "",
        "", process.getTurnaroundTime(), "",
        "", process.getWaitTime(), "",
        "", process.getResponseTime(), "");
    }
    System.out.printf("+%s+\n", "-".repeat(98));
    System.out.printf("Average Turnaround Time: %2.2f\n", getAvgTurnaroundTime(jobs));
    System.out.printf("Average Wait Time:       %2.2f\n", getAvgWaitTime(jobs));
    System.out.printf("Average Response Time:   %2.2f\n", getAvgResponseTime(jobs));
  }

  private boolean isIdle() {
    return this.isIdle;
  }

  private void setCPUTime(int cpuTime) {
    this.cpuTime = cpuTime;
  }

  private void setTimeQuantum(int timeQuantum) {
    this.baseTimeQuantum = timeQuantum;
  }

  private void setIsIdle(boolean idle) {
    this.isIdle = idle;
  }
}
