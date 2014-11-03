angular.module("proximal").controller "StandardsCtrl", ($log,$cookieStore,$scope) ->
  $scope.page = "Standards Page"

angular.module("proximal").controller "AddStandardCtrl", [
  "$log"
  "$scope"
  "$modalInstance"
  "prox.common"
  ($log, $scope, $modalInstance, common)->

    $scope.clear = ->
      $scope.standard.dateValid = null
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
   
    $scope.format = 'mediumDate'

    $scope.subjects = [
      "math"
      "english"
      "science" 
    ]

    $scope.languages = [
      "English"
      "Spanish"
      "French"
    ]


    $scope.availableEducationLevels = common.educationLevels

    $scope.ok = ->
      $log.debug($scope.educationLevels)
      $modalInstance.close($scope.standard, $scope.educationLevels)

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")
]
