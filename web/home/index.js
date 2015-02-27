'use strict()';

require("./home.html");
var app = require('angular').module('proximal2');
app.controller('HomeController', ['$log', require('./home_controller')]);
