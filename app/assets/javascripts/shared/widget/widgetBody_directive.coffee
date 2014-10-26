angular.module('proximal').directive "proxWidgetBody", [
  -> 
    return { 
      requires: '^proxWidget'
      scope: {
        loading: '@?'
      }
      transclude: true
      template: '<div class="widget-body"><prox-loading ng-show="loading"></prox-loading><div ng-hide="loading" class="widget-content" ng-transclude></div></div>'
      restrict: 'E'
    }
]
