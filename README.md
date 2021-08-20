# Mikser

Mikser is an example coin mixer that mixes JobCoins.

Here is how to use this package:

## Pre-requisites

### Install maven

On Mac:

`brew install mvn`

## Build the project

From the workspace root (e.g. `~/mikser`), run:

`mvn package`

This will build and run the unit tests. Execution is complete when you see
`[INFO] BUILD SUCCESS` at the end.

## Execution

The Java application can be executed via this command:

`mvn compile exec:java -Dexec.mainClass=com.mixer.MixerCLI`

Follow the prompts, then let Mikser do its thing. Don't exit the command-line interface until the payout is complete.

## Sample Execution

```
----------------------------------------------------------
Enter the number of destination addresses you wish to use
----------------------------------------------------------
2
----------------------------------------------------------
Enter destination address, percent distribution (e.g. 0.33 for 33%), and payout time delay (in seconds) comma separated, e.g. abc,0.33,10
----------------------------------------------------------
uno,0.7,4
----------------------------------------------------------
Enter destination address, percent distribution (e.g. 0.33 for 33%), and payout time delay (in seconds) comma separated, e.g. abc,0.33,10
----------------------------------------------------------
dos,0.3,9
----------------------------------------------------------
Enter fee as percent of total amount (min=0.0 and max=0.03, i.e, 3%)
----------------------------------------------------------
0.005
Please send your jobcoins to 0cae1ca5ad804aa9832c443ed6f7a29c for mixing

[pool-1-thread-2] INFO Mixer - Moved 9.950 jobcoins from depositAddress = 0cae1ca5ad804aa9832c443ed6f7a29c to house, with 0.050 jobcoins in fees
[pool-1-thread-2] INFO Mixer - Scheduled payout for uno with 4 sec delay
[pool-1-thread-2] INFO Mixer - Scheduled payout for dos with 9 sec delay
[pool-1-thread-1] INFO PayoutHandler - Moved 6.9650 jobcoins from bWlrc2VyaG91c2VhZGRyZXNz to uno uno 
[pool-1-thread-3] INFO PayoutHandler - Moved 2.9850 jobcoins from bWlrc2VyaG91c2VhZGRyZXNz to dos
```

## Notes

- User input validation is not currently built-in. Payout distribution is expected to add up to 1 (i.e., 100%).
- The clearance, fee, and payout transactions are not atomic and failures are not handled. Use at your own risk ;)
- The mixer will stop processing the deposit address after one hour of providing the user with the deposit address.