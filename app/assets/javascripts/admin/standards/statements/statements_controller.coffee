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
    $log.debug "getting the statement for " + $stateParams.statementId
    
    $scope.addStatement = ->
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/standards/statements/add_statement.html"
        controller: "AddStatementCtrl"
      })

      modalInstance.result.then((statement)->
        $log.debug "add the statement"
        $log.debug statement
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

    $scope.ok = ->
      $modalInstance.close()

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")

]

