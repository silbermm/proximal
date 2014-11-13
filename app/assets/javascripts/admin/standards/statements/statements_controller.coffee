angular.module("proximal").controller "StatementsCtrl",[
  "$log"
  "$cookieStore"
  "$scope"
  "$state"
  "$stateParams"
  "$modal"
  "standardsService"
  ($log,$cookieStore,$scope,$state,$stateParams,$modal,standardsService) ->
    $scope.page = "Statements Page"
    
    standardsService.getStatements($stateParams.id).success((data)->
      $scope.statements = data.statements
    ).error((data,status)->
      $log.error(data)
    )
    
    $scope.addStatement = ->
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/standards/statements/add_statement.html"
        controller: "AddStatementCtrl"
      })

      modalInstance.result.then((statement)->
        standardsService.addStatement($stateParams.id, statement).success((data,status,headers,config)->
          $scope.statements.push(data.statement)
        ).error((data,status,headers,config)->
          $log.error(data)
          $log.error(status)
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
  ($log,$scope,$modalInstance,common)-> 
    $scope.availableEducationLevels = common.educationLevels

    $scope.edu = {}
    $scope.edu.levels = {}

    $scope.ok = ->
      $modalInstance.close($scope.edu)

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")

]

