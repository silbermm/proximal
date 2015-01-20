AssesmentController = ($scope,$log,Child) ->
  

  $scope.test = "test"
  $log.debug("Hello from the assesment controller")

  $scope.begin = ->
    $log.debug("Beginning a new assesment!")

angular.module('proximal').controller "AssesmentCtrl",['$scope','$log','Child', AssesmentController]
