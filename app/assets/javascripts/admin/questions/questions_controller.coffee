angular.module("proximal").controller "QuestionsCtrl",[
  "$log"
  "$scope"
  "$state"
  "$stateParams"
  "$modal"
  "toaster"
  "uiGridConstants"
  "prox.common"
  "questionsService"
  ($log,$scope,$state, $stateParams, $modal, toaster, uiGridConstants,common, questionsService) ->
    $scope.page = "Questions Page"
  
    questionsService.getQuestions().success((data)->
      #$scope.gridOptions.data = data
      $scope.questions = data
    ).error((data)->
      $log.error(data)
    )

    $scope.gridOptions = {
      showFooter: true,
      enableFiltering: true,
      enableRowSelection: true,
      enableSelectAll: true,
      enableSorting: true,
      selectionRowHeaderWidth: 35,
      multiSelect: true,
      columnDefs: [
         { name: 'id' },
         { name: 'text'},
         { name: 'picture'}
         { name: 'standards'}
      ],
    }

    $scope.addQuestion = ->
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/questions/add_question.html"
        controller: "AddQuestionCtrl"
        resolve: {
          "obj" : {}
        }
      })
      modalInstance.result.then((question)->
        #add the question
        questionsService.addQuestion(question).success((data)->
          toaster.pop('success', "Success", "Added the question") 
          $scope.gridOptions.data.push(data); 
        ).error((data,status)->
          toaster.pop('error', "Failure", "Unable to add the question" + data);
        )
        return
      )
    return

]
angular.module("proximal").controller "AddQuestionCtrl",[
  "$log"
  "$scope"
  "$modalInstance"
  "prox.common"
  "standardsService"
  "obj"
  ($log,$scope,$modalInstance,common,standardsService, obj)->
    
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
      $log.debug($scope.select2.educationLevels)
      if(_.isUndefined($scope.select2.educationLevels) || _.isNull($scope.select2.educationLevels) || _.isEmpty($scope.select2.educationLevels)) 
        $log.debug("educationLevels is undefined!")
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
      $log.debug($scope.availableStatements)
      return

    $scope.showEducationLevels = ->
      return (!_.isUndefined($scope.standardSelected))

    $scope.showStatements = ->
      return !_.isUndefined($scope.availableStatements)

    $scope.ok = ->
      pic = if angular.isDefined($scope.picture) then $scope.picture.base64 else null 
      statements = []
      statements = _.map($scope.select2.statements, (st)->
        st.statement
      )
      $modalInstance.close({
        "text" : $scope.question.text
        "picture" : pic
        "statements" : statements
      })

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")

]
