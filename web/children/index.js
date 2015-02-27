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


//Assessment Service
app.factory("Assesments",['$resource',require('./assessments/assessment_service')]);

// View Assessments
require('./assessments/assessment.html');
app.controller("AssessmentCtrl",['$log', 'standardsService', 'Assesments', '$stateParams', '$modal', require('./assessments/assessment_controller')]);

// New Assessment
require('./assessments/new/new_assessment.html');
app.controller("NewAssessmentCtrl", [ '$log', 'Assesments', '$modalInstance', 'items', require('./assessments/new/new_assessment_controller')]);

require('./homework');
