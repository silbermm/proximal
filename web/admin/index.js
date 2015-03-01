var app = require('angular').module('proximal2');

require("./admin.html");
app.controller("AdminCtrl",[ "$log","$cookieStore", "standardsService", "$modal", require('./admin_controller')]); 

require('../questions');
