angular.module("proximal").directive("proxFooter", ($log) ->
  {
    restrict: "E",
    scope: {},
    replace: true,
    templateUrl: "../shared/footer/footer.html",
    link: (scope,element,attr)->
      scope.copyright = new Date().getFullYear()
  }
)
