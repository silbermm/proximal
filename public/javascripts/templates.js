angular.module('templates-main', ['../home/home.html', '../login/login.html', '../shared/footer/footer.html', '../shared/header/header.html']);

angular.module("../home/home.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../home/home.html",
    "<h1>{{ctrl.home}}</h1>");
}]);

angular.module("../login/login.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../login/login.html",
    "<prox-header></prox-header><div class=container-fluid><form class=form-horizontal></form></div><prox-footer></prox-footer>");
}]);

angular.module("../shared/footer/footer.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../shared/footer/footer.html",
    "<footer><style>body { padding-bottom: 70px; }</style><nav class=\"navbar navbar-default navbar-fixed-bottom\" role=navigation style=background-color:#fff><div class=container-fluid><div class=navbar-header><span class=navbar-brand><span class=\"small copyright\">Poximal Learning LLC {{copyright}}</span></span></div></div></nav></footer>");
}]);

angular.module("../shared/header/header.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../shared/header/header.html",
    "<header><nav class=\"navbar navbar-default navbar-fixed-top\" role=navigation><div class=container-fluid><div class=navbar-header><a class=navbar-brand>Proximal</a></div></div></nav></header>");
}]);
