# DNS Relay

Checking local database (eg. dnsrelay.txt) first:

* Case 1: for IP address 0.0.0.0,  sending back “no such name” (reply code =0011)
* Case 2: for domain name included in the database, sending back corresponding IP address
* Case 3: for domain name not included in the database, forward query to local DNS server

Test: `nslookup [Domain] 127.0.0.1`