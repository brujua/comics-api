#! /bin/bash

#Serving in port 80 without needing to run app with super user permissions
sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-ports 8080
sudo iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 --dport 80 -j REDIRECT --to-ports 8080

chmod +x mvnw
./mvnw install