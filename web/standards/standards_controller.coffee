angular.module("proximal").controller "StandardsCtrl",[
  "$log"
  "$scope"
  "$state"
  "$stateParams"
  "$modal"
  "standardsService"
  "toaster"
  ($log,$scope, $state, $stateParams,$modal,standardsService, toaster) ->

    initWithId = ->
      standardsService.getStandard($stateParams.id).success((data,status,headers,config)->
        $scope.standard = data.standard
        $scope.educationLevels = data.levels
      ).error((data,status,headers,config) ->
        toaster.pop('error', "Failure", "Unable to get standareds");
      )
      standardsService.getStatements($stateParams.id).success((data)->
        $scope.statements = data.statements
      ).error((data,status)->
        $log.error(data)
      )

    init = ->
      standardsService.getAllStandards().success((data,status)->
        $scope.standards = data
      ).error((data,status)->
        toaster.pop('error', "Failure", "Unable to get standards")
      )

    $scope.deleteStandard = ->
      # confirmation dialog!
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/standards/delete_standard.html",
        controller: ($scope, $modalInstance)->
          $scope.ok = -> 
            $modalInstance.close();

          $scope.cancel = ->
            $modalInstance.dismiss('cancel'); 
      })
      modalInstance.result.then( ()->
        standardsService.removeStandard($scope.standard.id).success((data)-> 
          toaster.pop("success", null, "Successfully deleted standard") 
          $scope.standards = _.filter($scope.standards, (st)->
            return st.id is not $scope.standard.id
          )
          $state.go("admin.standards")
        ).error((data)->
          $log.debug("Error: ")
          $log.debug(data)
        )
        return
      ,()->
        $log.info("dismissed delete modal")
        return
      )
 
    $scope.isDescriptionCollapsed = false
    $scope.isEducationCollapsed = false
   
    $scope.addStatement = ->
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/standards/add_statement.html"
        controller: "AddStatementCtrl"
        resolve: {
          standardId: ->
            return $stateParams.id
        }
      })
      modalInstance.result.then((statement)->
        standardsService.addStatement($stateParams.id, statement).success((data,status,headers,config)->
          $scope.statements.push(data)
          toaster.pop('success', null, "Successfully added the statement")
        ).error((data,status,headers,config)->
          $log.error(data)
          $log.error(status)
          toaster.pop('error',null, "Failed to add the statement: " + data.message)
        )
        return
      )
    
    $scope.addStandard = ->
      $log.debug("create a new standard")
      modalInstance = $modal.open({
        templateUrl: '../assets/javascripts/admin/standards/add_standard.html',
        controller: 'AddStandardCtrl'
      })

      addStandard = (s) ->
        standardsService.addStandard(s).success((data,success,headers,config) ->
          $scope.standards.push(data.standard);
          toaster.pop('success', null, "Successfully added the new standard")
        ).error((data,status,headers,config) ->
          $log.error("Unable to add standard: " + data)
          toaster.pop('error',null, "Failed to add the standard: " + data.message)
        )

      modalInstance.result.then((standard)->
        addStandard(standard)
        return
      )
 
    if !_.isUndefined($stateParams.id)
      initWithId()
    else
      init()

    return
]
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
angular.module("proximal").controller "AddStatementCtrl",[
  "$log"
  "$scope"
  "$modalInstance"
  "prox.common"
  "standardsService"
  "standardId"
  ($log,$scope,$modalInstance,common,standardsService,standardId)-> 
    $scope.availableEducationLevels = common.educationLevels 
    $scope.edu = {
      statement: {}
    }
    $scope.edu.levels = {}
    standardsService.getStandard(standardId).success((data)->
      $scope.edu.statement.subject = data.standard.subject
      $scope.availableEducationLevels = _.filter($scope.availableEducationLevels, (lev) ->
        has = _.find(data.levels, (standardLevel) ->
          standardLevel.description is lev.description
          return
        )
        has is `undefined`
      )
    ).error((data)->
      $log.error(data)
    )
    $scope.ok = ->
      $modalInstance.close($scope.edu)

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")
]

