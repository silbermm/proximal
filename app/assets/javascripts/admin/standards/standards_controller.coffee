angular.module("proximal").controller "StandardsCtrl", ($log,$cookieStore,$scope,$rootScope, $state, $stateParams, standardsService, toaster) ->
  $scope.page = "Standards Page"
  $log.debug("getting the standard for " + $stateParams.id)

  standardsService.getStandard($stateParams.id).success((data,status,headers,config)->
    $scope.standard = data.standard
    $scope.educationLevels = data.levels
  ).error((data,status,headers,config) ->
    toaster.pop('error', "Failure", "Unable to get standareds");
  )
  
  $scope.isDescriptionCollapsed = false;
  $scope.isEducationCollapsed = false
  return

angular.module("proximal").controller "AddStandardCtrl", [
  "$log"
  "$scope"
  "$modalInstance"
  "prox.common"
  "toaster"
  ($log, $scope, $modalInstance, common,toaster)->

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
