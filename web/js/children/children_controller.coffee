angular.module("proximal").controller "ChildrenCtrl", ($log,$cookieStore,$scope) ->
  $scope.page = "Children Page"
  $log.debug($scope.state)
