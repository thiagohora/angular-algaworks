#!/bin/bash

heroku config:set JDBC_DATABASE_URL=$1
heroku config:set JDBC_DATABASE_USERNAME=$2
heroku config:set JDBC_DATABASE_PASSWORD=$3

echo "Setup finished!"
