angular.module("proximal").controller "QuestionsCtrl",[
  "$log"
  "$scope"
  "$state"
  "$stateParams"
  "$modal"
  "toaster"
  "prox.common"
  "questionsService"
  "standardsService"
  ($log,$scope,$state, $stateParams, $modal, toaster,common, questionsService, standardsService) ->

    initQuestionPage = ->
      $scope.question = questionsService.getQuestion($stateParams.questionId)
      $scope.availableStandards = standardsService.standards().query()
      $scope.availableStandards.$promise.then((s)->
        $scope.question.$promise.then((q)->
          if !_.isUndefined($scope.question.statements[0]) 
            $scope.standard = _.find(s, (stan)->
              return stan.id is $scope.question.statements[0].standardId 
            )
        )
      )
    init = ->
      $scope.questions = questionsService.getQuestions()

    if !_.isUndefined($stateParams.questionId)
      initQuestionPage()       
    else
      init() 
    
    $scope.availableEducationLevels = common.educationLevels
    
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
        q = questionsService.question(question)
        $log.debug(q) 
        q.$save((ques,headers)->
          $scope.questions.push(ques)
          toaster.pop('success', "Success", "Added the question") 
          return
        ,(err)->
          toaster.pop('error', "Failure", "Unable to add the question" + err)
          return
        )
        return
      )
    return

]
angular.module("proximal").controller "AddQuestionCtrl",[
  "$log"
  "$scope"
  "prox.common"
  "standardsService"
  "$modalInstance"
  "obj"
  ($log,$scope,common,standardsService,$modalInstance,obj)->
    
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
