AssesmentController = ($scope,$log,Child,Assesments, $stateParams) ->
  
  $scope.test = "test"
  $log.debug($stateParams.id)

  $scope.child = Child.get({"id": $stateParams.id})

  $scope.begin = ->
  	Assesments.save($scope.child)

angular.module('proximal').controller "AssesmentCtrl",['$scope','$log','Child', 'Assesments', '$stateParams', AssesmentController]
