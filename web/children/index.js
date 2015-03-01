'use strict()';

require('./children.html');
var app = require("angular").module("proximal2");

app.controller("ChildrenCtrl", ['$log', 'Child', '$modal', require("./children_controller")]);
app.factory("Child", [ "$log","$resource", require('./child_service')]);
app.directive("childPicture", ["$log", require('./child_picture_directive')]);

// Add a child
require('./add/add_child.html');
app.controller("AddChildCtrl",[ "$scope","$log","$modalInstance","Child","prox.common", require('./add/add_child_controller')]);

// View a child
require('./view/view_child.html');
app.controller("ViewChildCtrl", [ "$log", "$window", "$stateParams", "personService", require('./view/view_child_controller')]); 

require('./assessments');
require('./homework');
