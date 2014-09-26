angular.module("proximal").directive("proxHeader", ($log)->
  {
    restrict: "E",
    scope: {copyright : '='},
    replace: true,
    templateUrl: "../shared/header/header.html",
    controller: "HeaderCtrl", 
    link: (scope, element, attr)->
      $log.debug(element)
       
  }
)
