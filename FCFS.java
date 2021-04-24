import java.util.ArrayList;
import java.util.Iterator;

public class FCFS {
  private int cpuTime;
  private boolean isIdle;
  private ArrayList<Process> fcfsProcesses, readyQueue, finishedProcesses;

  public FCFS(ArrayList<Process> processes) {
    this.setCPUTime(0);
    this.fcfsProcesses = new ArrayList<>();
    this.readyQueue = new ArrayList<Process>();
    this.finishedProcesses = new ArrayList<Process>();

    /**
      when passing objects, their references are passed
      instead of their values
      we have to clone/deep copy them to avoid mutating
      the original
    */
    for (Process p : processes) {
      this.fcfsProcesses.add(p.clone());
    }
  }

  public void runFCFS() {
    System.out.println("\n+" + "-".repeat(45) + "+");
    System.out.println("|      First-Come First-Serve Scheduling      |");
    System.out.println("+" + "-".repeat(45) + "+");

    simulateFCFS();
    printSummary(finishedProcesses);
  }

  public void simulateFCFS() {
    setIsIdle(true); // CPU is idle, waiting for jobs
    Process currentProcess = new Process();
    int currProcessIter = 0; // track number of processes completed
    int estimatedCPUTime = getTotalBurstTime(this.fcfsProcesses); // the total cpu time needed based on burst times

    // run until all processes have been ran and no processes remain in the ready queue
    while (this.getCPUTime() <= estimatedCPUTime+1) {
      // check if the ready queue has a job waiting to be executed
      if (!this.readyQueue.isEmpty()) {
        // make sure our CPU is idle to take jobs
        if (isIdle()) {
          // run the process / job
          setIsIdle(false);
          // remove from readyQueue and set as the process currently being worked on
          currentProcess = this.readyQueue.remove(0);
          // set its start time
          currentProcess.setStartTime(getCPUTime() - 1);
          currProcessIter++;
        }
      }

      // check if current CPU time allows for a process to be added into the ready queue
      if (!this.fcfsProcesses.isEmpty() && this.fcfsProcesses.get(0).getArrivalTime() <= getCPUTime()) {
        this.readyQueue.add(this.fcfsProcesses.remove(0));
      }

      // check if theres a process running
      if (!isIdle()) {
        // check if process is done running
        if (currentProcess.getRemainingBurstTime() == 0) {
          currentProcess.setCompletionTime(getCPUTime() - 1);
          currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
          currentProcess.setWaitTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
          currentProcess.setResponseTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
          finishedProcesses.add(currentProcess);
          setIsIdle(true);
        } else {
          currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
          System.out.printf("[CPU Time: %02d] CPU is working on PID %d\n", getCPUTime(), currentProcess.getPID());
          setCPUTime(getCPUTime() + 1);
        }
      } else {
        System.out.printf("[CPU Time: %02d] CPU is currently idle...\n", getCPUTime());
        setCPUTime(getCPUTime() + 1);
      }
    }
  }

  private int getCPUTime() {
    return this.cpuTime;
  }

  private int getTotalBurstTime(ArrayList<Process> jobs) {
    int sum = 0;
    for (Process job : jobs) {
      sum += job.getBurstTime();
    }
    return sum;
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
    System.out.println("\n>>> First-Come First-Serve (FCFS) Scheduling Summary <<<");
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

  private void setIsIdle(boolean idle) {
    this.isIdle = idle;
  }
}
