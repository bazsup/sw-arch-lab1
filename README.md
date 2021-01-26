# Simple web api

#### Table of contents
- [Build Application Guideline](#build-application-guideline)
- [Deployment Guideline](#deployment-guideline)
   - [Firewall configuation](#firewall-configuation)
   - [Setup Apache Server](#setup-apache-server)
   - [Send jar file via SCP](#send-jar-file-via-scp)
   - [Java Runtime Installation](#Java-Runtime-Installation)
   - [Other command](Other-command)

## Build Application Guideline

1. Build command
```
./mvnw clean package
```
- the result will appear in `target/` directory `simple-web-api-0.0.1-SNAPSHOT.jar`


## Deployment Guideline

we will use **Ubuntu Server 20.04 LTS** as web server

after create VM you can follow the step below

### Firewall configuation

- Allow SSH connection
```
sudo ufw allow ssh
```

- Allow HTTP connection
```
sudo ufw allow http
# for deny http connection
sudo ufw deny http
```

- Enable UFW
```
sudo ufw enable
```

- Checking UFW Status and Rules
```
sudo ufw status verbose
```

### Setup Apache Server

1. Update package
```
sudo apt update
```

2. Install `apache2` package
```
sudo apt install apache2
```

3. Adjust firewall
- check profile
```
sudo ufw app list
```

```
# Output
Available applications:
  Apache
  Apache Full
  Apache Secure
  OpenSSH
```
- select Apache to use port 80
```
sudo ufw allow 'Apache'
```

4. Checking firewall status
```
sudo ufw status
```

5. Reverse proxy to port 8080

edit this find
```
sudo vim /etc/apache2/sites-enabled/example.com.conf
```
with this content
```
<VirtualHost *:80>
  ProxyPreserveHost On
  ProxyRequests Off
  ServerName example.com
  ServerAlias example.com
  ProxyPass / http://localhost:8080/
  ProxyPassReverse / http://localhost:8080/
</VirtualHost>
```

> you need to replace `example.com` with your domain

> VIM replace command `:%s/example.com/your.hostname/g`

and enable proxy mod with this command
```
sudo a2enmod proxy && sudo a2enmod proxy_http
```

then, reload apache with new config
```
sudo /etc/init.d/apache2 reload
```

### Send jar file via SCP

- You can following this command
```
scp simple-web-api.jar user@vhostname:
```

### Java Runtime Installation

1. Update package index
```bash
sudo apt update
```

2. Install java runtime
```bash
sudo apt install default-jre
```

3. Verify java is installed
```
java -version
```

4. Start SpringBoot Application in background process
```
java -jar application.jar &
# or
java -jar application.jar -server.port=80 &
```

#### notes

start.sh
```
#!/bin/bash
java -jar myapp.jar & echo $! > ./pid.file &
```

stop.sh
```
#!/bin/bash
kill $(cat ./pid.file)
```

start_silent.sh
```
#!/bin/bash
nohup ./start.sh > foo.out 2> foo.err < /dev/null &
```

### Other command

Command for check java process 
```
pidof java
```

Kill process
```
kill <pid>
```



## Azure Cli

pre-required
- azure cli 👉 https://docs.microsoft.com/en-us/cli/azure/install-azure-cli

- Login
```
az login
```

### Resource group

- List resource group
```
az group list
```

- Create
```
az group create --location group-project1
  --name
```

- Delete
```
az group wait --name group-project1 --deleted
```
### Virtual Machine Image

- Search image
```
az vm image list -p canonical -o table --all | grep 20_04-lts
```
> reference: https://github.com/Azure/azure-cli/issues/13320#issuecomment-649867249

Then copy `urn` you prefer.
For example `Canonical:0001-com-ubuntu-server-focal:20_04-lts-gen2:20.04.202101191`

### Disk

- List disks
```
az disk list
```

- Create
```
az disk create --name lab1_DataDisk_0 \
  --resource-group group-project1 \
  --size-gb 8
```

### Virtual Machine

- List size
```
az vm list-sizes -l southeastasia
```

- Create
```
az vm create --resource-group group-project1 \
  --name lab1 \
  --image "Canonical:0001-com-ubuntu-server-focal:20_04-lts-gen2:20.04.202101191" \
  --authentication-type password \
  --admin-username azureuser \
  --admin-password SecurePassw0rd \
  --attach-data-disks lab1_DataDisk_0 \
  --size Standard_B1s \
  --output json \
  --verbose
```

### Domain Name Server (DNS)

- List
```
az network dns zone list
```

- Show detail
```
az network dns zone show --name demo.example.com \
  --resource-group group-project1
```
- List records
```
az network dns record-set a list --resource-group group-project1 \
  --zone-name demo.example.com
```

- Create record-set
```
az network dns record-set a create --name @ \
  --resource-group group-project1 \
  --zone-name demo.example.com
```

- Show record-set
```
az network dns record-set a show --name @ \
  --resource-group group-project1 \
  --zone-name demo.example.com
```

- Delete record-set
```
az network dns record-set a delete --name @ \
  --resource-group group-project1 \
  --zone-name demo.example.com
```

- Add record
```
az network dns record-set a add-record --ipv4-address 192.168.1.11 \
  --record-set-name @ \
  --resource-group group-project1 \
  --zone-name demo.example.com \
  --ttl 300
```

- Remove record
```
az network dns record-set a remove-record --ipv4-address * \
  --record-set-name @ \
  --resource-group group-project1 \
  --zone-name demo.example.com
```
