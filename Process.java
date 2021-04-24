public class Process implements Cloneable {

  private int PID, arrivalTime, burstTime, startTime, completionTime, turnaroundTime, waitTime, responseTime, remainingBurstTime;

  public Process() {
    this.setPID(0);
    this.setArrivalTime(0);
    this.setBurstTime(1);
    this.setRemainingBurstTime(1);
    this.setStartTime(-1);
  }

  public Process(int PID, int arrivalTime, int burstTime) {
    this.setPID(PID);
    this.setArrivalTime(arrivalTime);
    this.setBurstTime(burstTime);
    this.setRemainingBurstTime(burstTime);
    this.setStartTime(-1);
  }

  /**
    we are passing an ArrayList of Process objects to
    each scheduling algorithm, but when passing, object references
    are passed rather than object values
    So, we have to clone/deep copy the objects first
  */
  @Override
  public Process clone() {
    Process clonedProcess = null;
    try {
      clonedProcess = (Process) super.clone();
      clonedProcess.setPID(this.PID);
      clonedProcess.setArrivalTime(this.arrivalTime);
      clonedProcess.setBurstTime(this.burstTime);
      clonedProcess.setStartTime(this.startTime);
      clonedProcess.setCompletionTime(this.completionTime);
      clonedProcess.setTurnaroundTime(this.turnaroundTime);
      clonedProcess.setWaitTime(this.responseTime);
      clonedProcess.setResponseTime(this.responseTime);
      clonedProcess.setRemainingBurstTime(this.remainingBurstTime);
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    return clonedProcess;
  }

  // Setters
  public void setPID(int PID) {
    this.PID = PID;
  }

  public void setArrivalTime(int arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public void setBurstTime(int burstTime) {
    this.burstTime = burstTime;
  }

  public void setRemainingBurstTime(int burstTime) {
    this.remainingBurstTime = burstTime;
  }

  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }

  public void setCompletionTime(int completionTime) {
    this.completionTime = completionTime;
  }

  public void setTurnaroundTime(int turnaroundTime) {
    this.turnaroundTime = turnaroundTime;
  }

  public void setWaitTime(int waitTime) {
    this.waitTime = waitTime;
  }

  public void setResponseTime(int responseTime) {
    this.responseTime= responseTime;
  }

  // Getters
  public int getPID() {
    return this.PID;
  }

  public int getArrivalTime() {
    return this.arrivalTime;
  }

  public int getBurstTime() {
    return this.burstTime;
  }

  public int getRemainingBurstTime() {
    return this.remainingBurstTime;
  }

  public int getStartTime() {
    return this.startTime;
  }

  public int getCompletionTime() {
    return  this.completionTime;
  }

  public int getTurnaroundTime() {
    return this.turnaroundTime;
  }

  public int getWaitTime() {
    return this.waitTime;
  }

  public int getResponseTime() {
    return this.responseTime;
  }

  @Override
  public String toString() {
    String processInfo = String.format("[PID %d] | Arrival Time %d | Burst Time: %d | Remaining Burst Time: %d | Start Time: %d | Completion Time: %d | Turnaround Time: %d | Wait Time: %d | Response Time: %d",
      this.getPID(), this.getArrivalTime(), this.getBurstTime(), this.getRemainingBurstTime(), this.getStartTime(), this.getCompletionTime(), this.getTurnaroundTime(), this.getWaitTime(), this.getResponseTime()
    );
    return processInfo;
  }

}
