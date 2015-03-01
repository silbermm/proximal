'use strict()';
var _ = require('lodash');
var angular = require('angular');

//require("bootstrap-webpack");
//require("font-awesome-webpack");
require('./styles/main.less');
require("angular-resource");
require("angular-animate");
require("angular-sanitize");
require("angular-cookies");
require("ui-bootstrap");
require("ui-router");
require("angular-toaster");

require("select2");
require("ui-select2");
require("ng-file-upload");

angular.module("proximal2", [
    'ngResource', 
    'ngSanitize', 
    'ngAnimate', 
    'ngCookies', 
    'ui.router', 
    'ui.bootstrap',
    'ui.select2',
    'ngFileUpload',
    'toaster']);

require('./startup');
