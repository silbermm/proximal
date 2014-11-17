angular.module("proximal").controller "StatementsCtrl",[
  "$log"
  "$cookieStore"
  "$scope"
  "$state"
  "$stateParams"
  "$modal"
  "standardsService"
  "toaster"
  "prox.common"
  ($log,$cookieStore,$scope,$state,$stateParams,$modal,standardsService,toaster,common) ->
    $scope.page = "Statements Page"
   
    $scope.edlevels = common.educationLevels

    standardsService.getStatements($stateParams.id).success((data)->
      $scope.statements = data.statements
    ).error((data,status)->
      $log.error(data)
    )
    
    $scope.addStatement = ->
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/standards/statements/add_statement.html"
        controller: "AddStatementCtrl"
        resolve: {
          standardId: ->
            return $stateParams.id
        }
      })

      modalInstance.result.then((statement)->
        standardsService.addStatement($stateParams.id, statement).success((data,status,headers,config)->
          $scope.statements.push(data.statement)
          toaster.pop('success', null, "Successfully add the statement")
        ).error((data,status,headers,config)->
          $log.error(data)
          $log.error(status)
          toaster.pop('error',null, "Failed to add the statement: " + data.message)
        )
        return
      )
     
    return
  
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

