#!/usr/bin/env bash

su - vagrant -c 'cd /vagrant && bower install && npm install && grunt watchd'
#su - vagrant -c 'bower install' -p
#su - vagrant -c 'npm install' -p
#su - vagrant -c 'grunt watchd' -p
