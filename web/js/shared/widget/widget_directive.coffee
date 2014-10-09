#angular.module("proximal").directive('proxWidget',proxWidget)

proxWidget = ->
  transclude: true
  template: '<div class="widget" ng-transclude></div>'
  restrict: 'EA'
