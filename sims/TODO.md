


We need metrics on the following workloads for RocketChip.

Please do the following.

1) open a termianl and CD into sims/verilator. <ake sure you are sourced (source env.source in the top level dir).
2) make sure that you have initialized the ChaosCore-riscv-tests repo. That is, make sure that its not empty when you CD into it because its a submodule
3) make sure that the ChaosCore-riscv-tests repo is fully up to date. For this, run: git checkout master + git pull in the ChaosCore-riscv-test repo
4) once you have ensured that the repo is up to date, we need to kick off the simulations. For this, we will run:
5) time + workload command > rocketchip_<workload>.txt. This will time how long it takes the workload to run on your system, and will store the contents into that .txt file.
6) I've already placed these commands into the verilator makefile. hence, all you need to do is make benchmark-rocket
7) Write your results in results.csv in verilator/ 
8) Mess with various params, collect more results, see what else you can come up with. More data is always good. 


