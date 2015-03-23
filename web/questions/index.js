'use strict()';

var app = require("angular").module("proximal2");


// Question Service
app.factory("Question", [ "$log", "$resource", "$http", require('./questions_service')]);
 
// Question Controller
require("./questions.html");
app.controller("QuestionsCtrl", require('./questions_controller'));

app.directive("questions", require('./questions.directive'));

// Details
require("./details/detail_question.html");
app.directive("question", require('./details/question_directive'));
app.directive("questionDetails", ['$log', '$state', 'prox.common', require("./details/question_details")]);
app.directive("questionPicture", ['$log', '$q', require('./details/question_picture')]);
app.directive("questionAdd", ["$log", "prox.common", require('./details/question_add')]);

// Add
require("./add/add_question.html");
app.controller("AddQuestionCtrl",[ "$log", "$scope", "prox.common", "$upload","standardsService", "$modalInstance", require('./add/add_question_controller')]);

// Edit 
require("./edit/edit_question.html");
app.controller("EditQuestionsCtrl",[ "$log", "$scope", "$state", "$stateParams","prox.common","standardsService","Question", "toaster", require('./edit/edit_questions_controller')]);

