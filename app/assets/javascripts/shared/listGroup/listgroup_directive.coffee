ListGroupController = ($log)->


ListGroup = ($log)->
  restrict: "E"
  scope: {
    item: '=item'
  }
  transclude: true
  replace: true
  controller: "ListGroupCtrl as ctrl"
  templateUrl: "../assets/javascripts/shared/listGroup/listgroup.html"
  link : (scope,el,attr)->


angular.module("proximal").controller "ListGroupCtrl",["$log", ListGroupController]
angular.module("proximal").directive "listGroup", ["$log", ListGroup]
