#!/bin/bash

if [[ $# -ne 4 ]]
then
	echo "usage : $0 <db_admin_username> <db_admin_password> <jwtsecret> <keystorepass>"
	exit 1
fi

sed "s/ admin/ $1/g" api/database_scheme.sql.pattern > api/database_scheme.sql
sed -e "s/ defaultadmin/ $1/g"  -e "s/ defaultpassword/ $2/g" -e "s/ defaultjwtpassword/ $3/g" -e "s/ defaultkeystorepassword/ $4/g"  docker-compose.yml.pattern > docker-compose.yml

docker compose up --build -d

