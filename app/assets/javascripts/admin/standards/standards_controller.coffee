angular.module("proximal").controller "StandardsCtrl", ($log,$cookieStore,$scope) ->
  $scope.page = "Standards Page"

angular.module("proximal").controller "AddStandardCtrl", ($scope,$log,$modalInstance)->
  
  $scope.ok = ->
    $modalInstance.close({})

  $scope.cancel = ->
    $modalInstance.dismiss("cancel")
