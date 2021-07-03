# Set up ELK environment on Amazon EC2 Instance

The following instructions are explaining the steps required to run a quick ELK cluster on Docker network.
The all settings are really naive, so some improvements would be required if you want.

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

## Setup VPC

| Source                       | Type        | Protocol | Port Range          |
| ---------------------------- | ----------- | -------- | ------------------- |
| Security Group sg1234567890  | All Traffic | All      | All                 |
| Your Client IP Address(es)   | Custom TCP  | TCP      | 5601(Kibana)        |
| Your Client IP Address(es)   | Custom TCP  | TCP      | 9200(Elasticsearch) |

## Start containers

Deploy *docker-compose.yml* files on the host machines and from
inside the directory run:

```bash
 $ sudo /usr/local/bin/docker-compose up -d
```

## Fix Errors

 - If the Elasticsearch container within the Docker network complains as follows, run:

```text
bootstrap check failure [1] of [x]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```
```bash
 $ sudo sysctl -w vm.max_map_count=262144
```
