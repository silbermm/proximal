angular.module("proximal").controller "AddQuestionCtrl",[
  "$log"
  "$scope"
  "prox.common"
  "standardsService"
  "$modalInstance"
  ($log,$scope,common,standardsService,$modalInstance)->
    
    $scope.select2 = {}

    standardsService.getAllStandards().success((data)->
      $scope.availableStandards = data 
    ).error((data)->
      $log.error(data)
    )
    
    $scope.availableEducationLevels = common.educationLevels

    $scope.getStatements = ->
      $scope.select2.statements = []
      standardsService.getStatements($scope.standardSelected.id).success((data)->
        $scope.availableStatements = data.statements
      ).error((data)->
        $log.error("unable to get statements")
      )

    $scope.educationLevelChange = ->
      if(_.isUndefined($scope.select2.educationLevels) || _.isNull($scope.select2.educationLevels) || _.isEmpty($scope.select2.educationLevels)) 
        $scope.getStatements()
        return
      $scope.availableStatements = _.filter($scope.availableStatements, (s) ->
        test = _.filter(s.levels, (l)->
          found = _.find($scope.select2.educationLevels, (e)->
            return l.value is e.value    
          )
          return !_.isUndefined(found)
        )
        return test.length > 0
      ) 
      return

    $scope.showEducationLevels = ->
      return (!_.isUndefined($scope.standardSelected))

    $scope.showStatements = ->
      return !_.isUndefined($scope.availableStatements)

    $scope.ok = ->
      $scope.question.picture = if angular.isDefined($scope.picture) then $scope.picture.base64 else null 
      $scope.question.statements = []
      $scope.question.statements = _.map($scope.select2.statements, (st)->
        st.statement
      )
      $modalInstance.close($scope.question)

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")

]
