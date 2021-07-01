# Set up Storm Cluster on Local Docker Network

The following instructions are explaining the steps required to run a quick single node cluster on Docker network.
Having a single node dev cluster will help in testing out storm topologies.
The all scripts/settings are really naive, so some improvements would be required if you want.

## Docker Compose

Be sure that you have already installed Docker and docker-compose on your local machine
before running the following command. From inside this directory run:

```bash
 $ docker-compose up -d
```