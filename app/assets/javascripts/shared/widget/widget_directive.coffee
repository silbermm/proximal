angular.module("proximal").directive 'proxWidget',[
  ->
    return {
      transclude: true
      template: '<div class="widget" ng-transclude></div>'
      restrict: 'EA'
    }
]
