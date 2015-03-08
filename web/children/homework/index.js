'use strict()';

var app = require('angular').module("proximal2");

require('./homework.html');
app.controller("HomeworkCtrl", ["$log", '$modal', 'toaster', '$stateParams', 'standardsService', "Homework", require('./homework_controller')]);

app.factory("Homework",['$resource', require('./homework_service')]);

require('./add/add_homework.html');
app.controller("AddHomeworkCtrl", ["$log", "$modalInstance", "standardsService", "Child", "prox.common", "$stateParams", "items", require("./add/add_homework_controller")]);

require('./details/homework_details.html');
app.controller("HomeworkDetailsCtrl", ["$log", "standardsService", "Child", "Score", "prox.common", "$stateParams", require("./details/homework_details_controller")]);
