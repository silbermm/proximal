"use strict()";
(function(){

  var app = require('angular').module('proximal2');
  require('./resources.html');
  app.controller("ResourcesController", require('./resources.controller'));

  app.factory("Resources", require('./resources.service'));

  require("./resourceCompactView.html");
  app.directive("resourceCompactView", require('./resourceCompactView.directive'));

})();
