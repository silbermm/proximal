#!/usr/bin/env bash

apt-get update
apt-get install -y software-properties-common
apt-get install -y python-software-properties
apt-add-repository -y ppa:chris-lea/node.js
apt-get update
#apt-get install -y openjdk-7-jdk
apt-get install -y git
apt-get install -y nodejs
npm install -g grunt-cli
npm install -g bower
