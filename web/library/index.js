'use strict()';

require("./library.html");
var app = require("angular").module("proximal2");
app.controller("LibraryCtrl", ['$log', '$cookieStore', '$scope', require('./library_controller')]);
