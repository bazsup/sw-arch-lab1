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
