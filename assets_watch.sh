#!/usr/bin/env bash

su - vagrant -c 'cd /vagrant && bower install'
su - vagrant -c 'cd /vagrant && npm cache clean'
su - vagrant -c 'cd /vagrant && npm install grunt-contrib-imagemin'
su - vagrant -c 'cd /vagrant && npm install'
#su - vagrant -c 'bower install' -p
#su - vagrant -c 'npm install' -p
#su - vagrant -c 'grunt watchd' -p
