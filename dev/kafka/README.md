# Set up Kafka Cluster over Amazon EC2 Instances

## Set up EC2 instances

 - the instance type (e.g., t2.large)
 - the size and type of storage (e.g., 500GB st1)

## Install Docker and Docker Compose

Connect to both instances with SSH and run:

```bash
 $ yum install docker
 $ systemctl enable docker.service
 $ systemctl start docker.service
 $ sudo curl -L "https://github.com/docker/compose/releases/download/1.28.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
 $ sudo chmod +x /usr/local/bin/docker-compose
```

## Setup public IP addresses

*Node A*
 - Public IPv4 address: A.B.C.D
 - Public IPv4 DNS: ec2-A-B-C-D.us-east-1.compute.amazonaws.com
 - Private IPv4 address: 172.1.1.1

*Node B*
 - Public IPv4 address: E.F.G.H
 - Public IPv4 DNS: ec2-E-F-G-H.us-east-1.compute.amazonaws.com
 - Private IPv4 address: 172.2.2.2

## Setup VPC

| Source                       | Type        | Protocol | Port Range      |
| ---------------------------- | ----------- | -------- | --------------- |
| Security Group sg1234567890  | All Traffic | All      | All             |
| Your Client IP Address(es)   | Custom TCP  | TCP      | 2181(Zookeeper) |
| Your Client IP Address(es)   | Custom TCP  | TCP      | 9092(Kafka)     |

## Prepare the host

Connect to both instances with SSH and run:

```bash
 $ sudo chown 1001.1001 /data/zookeeper/
 $ sudo chown 1001.1001 /data/kafka/
```

## Start containers

Deploy *docker-compose.yml* files on the host machines and from
inside the directory run:

```bash
 $ sudo /usr/local/bin/docker-compose up -d
```

## Check if it’s working

From the external host/network you have configured in the VPC section,
try to reach both node 1:

```bash
 $ docker run --rm --tty confluentinc/cp-kafkacat \
kafkacat \
-b ec2-A-B-C-D.us-east-1.compute.amazonaws.com:9092 \
-L 
```

and node 2:

```bash
 $ docker run --rm --tty confluentinc/cp-kafkacat \
kafkacat \
-b ec2-E-F-G-H.us-east-1.compute.amazonaws.com:9092 \
-L 
```

## Create topics

From a client with Kafka installed (e.g., brew install kafka on macOS), run the following:

```bash
 $ kafka-topics --create \
--bootstrap-server ec2-A-B-C-D.us-east-1.compute.amazonaws.com:9092 \
--replication-factor 2 --partitions 10 \
--topic twitter
```
