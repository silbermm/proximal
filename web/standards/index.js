'use strict()';

var app = require('angular').module('proximal2');

// Standards
require('./standards.html');
require('./view_standard.html');
app.controller("StandardsCtrl",[ "$log", "$scope", "$state", "$stateParams", "$modal", "standardsService", "toaster", require('./standards_controller')]);

require("./standardView.html");
app.directive('standardView', require('./standardView.directive'));

// Add Standard
require('./add/add_standard.html');
app.controller("AddStandardCtrl", [ "$log", "$scope", "$modalInstance", "prox.common", "toaster", require('./add/add_standard_controller')]);

// Add Statement
require('./add/add_statement.html');
app.controller("AddStatementCtrl",[ "$log", "$scope", "$modalInstance", "prox.common", "standardsService", "standardId", require('./add/add_statement_controller')]); 
