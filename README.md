#  Java CPU Scheduling

The following Java files demonstrate CPU scheduling - more specifically First-Come First-Serve (FCFS) and Round Robin (RR).

## How to

Run `javac` to compile the code and then `java CPUScheduler <filepath> <time quantum>`

- `filepath` is the relative to a text file containing each process
- `time quantum` is the time slide for Round Robin

The text file of processes should hold information about the processes to be executed. Each line represents one process.

The format per line is as follows: `PX Y Z`

- where X is the process ID
- where Y is the process arrival time
- where Z is the process burst time

*yes, the "P" is part of the formatting*
