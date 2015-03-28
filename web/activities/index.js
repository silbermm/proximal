'use strict()';
(function(){
  require('./activities.html'); 
  var app = require("angular").module("proximal2");

  app.controller("ActivitiesController", require('./activities.controller'));  
  app.factory("Activities", require('./activities.service'));

  require('./add/add_activity.html'); 
  app.controller("AddActivityController", require('./add/addActivity.controller'));

  require('./edit/editActivity.html');
  app.controller("EditActivityController", require('./edit/editActivity.controller'));
  
  require('./activityWidget.html');
  app.directive("activityWidget", require('./activityWidget.directive'));

  require('./activitySets.html');
  app.directive("activitySetsWidget", require('./activitySetsWidget.directive'));

})();
