'use strict()';

var app = require('angular').module("proximal2");
app.config(['$stateProvider', '$urlRouterProvider', require("./routes")]);
app.controller('AppController', ['$scope', '$state', '$log', '$cookieStore', require("./app_controller")]);

