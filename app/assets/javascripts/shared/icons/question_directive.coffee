angular.module("proximal").directive 'questionMark',[
  ->
    return {
      transclude: true
      scope: {
        questionClick: "@"
      }
      template: "<a ng-click='questionClick'> <span class='fa-stack fa-sm'><i class='fa fa-circle fa-stack-2x'></i><i class='fa fa-question fa-stack-1x fa-inverse'></i></span> </a>"
      restrict: 'EA'
    }
]
