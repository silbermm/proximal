angular.module("proximal").controller "StandardsCtrl", ($log,$cookieStore,$scope) ->
  $scope.page = "Standards Page"

angular.module("proximal").controller "AddStandardCtrl", [
  "$log"
  "$scope"
  "$modalInstance"
  "prox.common"
  ($log, $scope, $modalInstance, common)->

    $scope.edu = {}

    $scope.clear = ->
      $scope.standard.dateValid = null
      return
     
    $scope.dateValidOpen = ($event)->
      $event.preventDefault()
      $event.stopPropagation()
      $scope.dateValidOpened = true

    $scope.repoDateOpen = ($event)->
      $event.preventDefault()
      $event.stopPropagation()
      $scope.repoDateOpened = true
    
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
      $modalInstance.close($scope.edu)

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")
]
