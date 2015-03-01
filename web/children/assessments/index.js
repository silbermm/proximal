var app = require("angular").module("proximal2");

// Templates
require('./new/new_assessment.html');
require('./assessment.html');

//Assessment Service
app.factory("Assesments",['$resource',require('./assessment_service')]);

// View Assessments
app.controller("AssessmentCtrl",['$log', 'standardsService', 'Assesments', '$stateParams', '$modal', require('./assessment_controller')]);

// New Assessment
app.controller("NewAssessmentCtrl", [ '$log', 'Assesments', '$modalInstance', 'items', require('./new/new_assessment_controller')]);


