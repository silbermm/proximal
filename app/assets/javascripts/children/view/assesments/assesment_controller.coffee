AssesmentController = ($scope,$log,Child,Assesments, standardsService, $stateParams) ->
  
  $scope.test = "test"
  $log.debug($stateParams.id)

  #$scope.child = Child.get({"id": $stateParams.id})
  standardsService.getAllStandards().success((data)->
    $scope.availableStandards = data 
   ).error((data)->
    $log.error(data)
   )

  $scope.begin = ->
  	Assesments.save({"childId": Number($stateParams.id), "standardId": $scope.standardSelected.id})

angular.module('proximal').controller "AssesmentCtrl",['$scope','$log','Child', 'Assesments', 'standardsService', '$stateParams', AssesmentController]
