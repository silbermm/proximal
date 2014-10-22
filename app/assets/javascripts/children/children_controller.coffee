angular.module("proximal").controller "ChildrenCtrl",($log,$cookieStore,$scope,personService, $modal) ->
  $scope.page = "Children Page"
 
  $scope.createChild = ->
    $log.debug "create a new child"
