angular.module('proximal').controller "AddChildCtrl",[
  "$scope"
  "$log"
  "$modalInstance"
  "Child"
  "prox.common"
  ($scope, $log, $modalInstance,Child,common) ->
 
    $scope.availableLevels = common.educationLevels

    $scope.clear = ->
      $scope.child.birthDate = null
      return
     
    $scope.open = ($event)->
      $event.preventDefault()
      $event.stopPropagation()
      $scope.opened = true

    $scope.dateOptions = {
      formatYear: 'yyyy',
      startingDay: 1
    }

    $scope.initDate = new Date("Jan 1, 2008")

    $log.debug($scope.initDate)
    
    $scope.format = 'mediumDate'

    $scope.ok = ->
      d = $scope.child.birthDate.getTime()/1000
      child = new Child({'firstName': $scope.child.firstName,'lastName': $scope.child.lastName,'birthDate': d, "educationLevel": $scope.child.gradeLevel})
      $modalInstance.close(child)

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")
]


