'use strict()';

require("./dashboard.html");

var app = require("angular").module("proximal2");
app.controller("DashboardCtrl", [ require("./dashboard_controller")]);

