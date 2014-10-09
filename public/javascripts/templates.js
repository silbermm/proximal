angular.module('templates-main', ['../dashboard/dashboard.html', '../home/home.html', '../login/login.html', '../shared/footer/footer.html', '../shared/header/header.html']);

angular.module("../dashboard/dashboard.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../dashboard/dashboard.html",
    "<div class=row><div class=\"col-lg-3 col-md-6 col-xs-12\"><div class=widget><div class=widget-body><div class=\"widget-icon green pull-left\"><i class=\"fa fa-users\"></i></div><div class=\"widget-content pull-left\"><div class=title>2</div><div class=comment>Children Registered</div></div><div class=clearfix></div></div></div></div><div class=\"col-lg-3 col-md-6 col-xs-12\"><div class=widget><div class=widget-body><div class=\"widget-icon orange pull-left\"><i class=\"fa fa-sitemap\"></i></div><div class=\"widget-content pull-left\"><div class=title>16</div><div class=comment>Assessments Taken</div></div><div class=clearfix></div></div></div></div><div class=\"col-lg-3 col-md-6 col-xs-12\"></div><div class=\"spacer visible-xs\"></div><div class=\"col-lg-3 col-md-6 col-xs-12\"></div></div><div class=row><div class=col-lg-6><div class=widget><div class=widget-header><i class=\"fa fa-tasks\"></i> Table of Data <a href=# class=pull-right>Clear</a></div><div class=\"widget-body medium no-padding\"><div class=table-responsive><table class=table><tbody></tbody></table></div></div></div></div><div class=col-lg-6><div class=widget><div class=widget-header><i class=\"fa fa-users\"></i> Collaborators <input placeholder=Search class=\"form-control input-sm pull-right\"><div class=clearfix></div></div><div class=\"widget-body medium no-padding\"><div class=table-responsive><table class=table><thead><tr><th class=text-center>ID</th><th>Username</th><th>Relationship</th><th>Account</th></tr></thead><tbody><tr><td class=text-center>1</td><td>Joe Bloggs</td><td>Brother</td><td>AZ23045</td></tr><tr><td class=text-center>2</td><td>Timothy Hernandez</td><td>Father</td><td>AU24783</td></tr><tr><td class=text-center>3</td><td>Joe Bickham</td><td>User</td><td>Friend</td></tr></tbody></table></div></div></div></div></div><div class=row><div class=col-lg-6><div class=widget><div class=widget-header><i class=\"fa fa-plus\"></i> Extras <button class=\"btn btn-sm btn-info pull-right\">Button</button><div class=clearfix></div></div><div class=widget-body><div class=message>This is a standard message which will also work the \".no-padding\" class, I can also <span class=error>be an error message!</span></div><hr><div class=message><a href=\"http://angular-ui.github.io/bootstrap/\" target=_blank>UI Bootstrap</a> is included, so you can use <a href=# tooltip=\"I'm a tooltip!\">tooltips</a> and all of the other native Bootstrap JS components!</div><hr><form class=form-horizontal role=form><div class=\"form-group has-feedback has-success\"><label for=label class=\"col-sm-2 control-label\">Inline Form</label><div class=col-sm-5><input class=\"form-control\"> <span class=\"fa fa-key form-control-feedback\"></span></div><div class=col-sm-5><div class=input-mask>I'm an input mask!</div></div></div></form></div></div></div><div class=col-lg-6><div class=widget><div class=widget-header><i class=\"fa fa-cog fa-spin\"></i> Loading Directive <a href=\"http://tobiasahlin.com/spinkit/\" target=_blank class=pull-right>SpinKit</a></div><div class=widget-body><prox-loading></prox-loading></div></div></div></div>");
}]);

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
    "<footer><style>body { padding-bottom: 70px; background: transparent; color: white; }</style><nav class=\"navbar navbar-default navbar-fixed-bottom\" role=navigation style=background-color:#fff><div class=container-fluid><div class=navbar-header><span class=navbar-brand><span class=\"small copyright\">Poximal Learning LLC {{copyright}}</span></span></div></div></nav></footer>");
}]);

angular.module("../shared/header/header.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("../shared/header/header.html",
    "<header><nav class=\"navbar navbar-default navbar-fixed-top\" role=navigation><div class=container-fluid><div class=navbar-header><a class=navbar-brand>Proximal</a></div></div></nav></header>");
}]);
