'use strict()';
(function(){
  require('./activities.html'); 
  require('./add/add_activity.html'); 
  var app = require("angular").module("proximal2");

  app.controller("ActivitiesController", require('./activities.controller'));  
  app.factory("Activities", require('./activities.service'));

  app.controller("AddActivityController", require('./add/addActivity.controller'));
  
})();