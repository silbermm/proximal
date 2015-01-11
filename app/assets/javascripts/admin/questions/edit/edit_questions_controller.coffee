angular.module("proximal").controller "EditQuestionsCtrl",[
  "$log"
  "$scope"
  "$state"
  "$stateParams"
  "prox.common"
  "standardsService"
  "Question"
  "toaster"
  ($log,$scope,$state,$stateParams,common,standardsService,Question,toaster)->
    
    GetAvailableStatments = ()->
      if !_.isUndefined($scope.question.statements[0]) 
        if !_.isUndefined($scope.standard)
          standardsService.getStatements($scope.standard.id).success((data)->
            $scope.availableStatements = data.statements
          ).error((data)->
             $log.error("unable to retrieve statements")
          ) 
        else
          $scope.availableStatements = []

   
    Standards = standardsService.standards 
    $scope.select2 = { statements : [] }

    $scope.init = ->
      $scope.question = Question.get("id": $stateParams.questionId, (ques) ->
        if (_.isUndefined($scope.question.statements))
          $log.debug("statements is undefined....")
          $scope.select2.statements = []
        else 
          $log.debug("there are statements!")
          $scope.select2.statements = $scope.question.statements
          $log.debug($scope.select2.statements) 
      )

      $scope.availableStandards = Standards().query()
    
      $scope.availableStandards.$promise.then((s)->
        $scope.question.$promise.then((q)->
          if !_.isUndefined($scope.question.statements[0]) 
            $scope.standard = _.find(s, (stan)->
              return stan.id is $scope.question.statements[0].standardId 
            )
            if !_.isUndefined($scope.standard)
              standardsService.getStatements($scope.standard.id).success((data)->
                $scope.availableStatements = data.statements
              ).error((data)->
                 $log.error("unable to retrieve statements")
              ) 
            else
              $scope.availableStatements = []


        )
      )

    

    $scope.update = ->
      $log.debug($scope.question)
      $scope.question.$update(()->
        toaster.pop('success', "Updated", "Successfully updated question")
        return
      ,()->
        toaster.pop('error', "Failed", "Did not update the question, contact the administrator")
        return 
      )

    $scope.delete = ->
      Question.remove("id": $scope.question.id, (q)->
        $log.debug("Record Deleted!")
        toaster.pop('success', "Deleted", "Successfully removed question " + $scope.question.id)
        $state.go("admin.questions")
        return
      , ->
        $log.debug("Unable to delete record")
        toaster.pop('error', "Failed", "Unable to delete this question")
        return 
      )
]
 
