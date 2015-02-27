angular.module("proximal").controller "QuestionsCtrl",[
  "$log"
  "$scope"
  "$state"
  "$stateParams"
  "$modal"
  "toaster"
  "prox.common"
  "Question"
  "standardsService"
  ($log,$scope,$state, $stateParams, $modal, toaster,common, Question, standardsService) ->

    $scope.init = ->
      $scope.questions = Question.query()

    $scope.availableEducationLevels = common.educationLevels
    
    $scope.addQuestion = ->
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/questions/add/add_question.html"
        controller: "AddQuestionCtrl"
      })
      modalInstance.result.then((question)->
        q = new Question(question)
        q.$save((ques,headers)->
          $scope.questions.push(new Question(ques))
          toaster.pop('success', "Success", "Added the question with ID " + ques.id) 
          return
        ,(err)->
          toaster.pop('error', "Failure", "Unable to add the question" + err)
          return
        )
        return
      )
    return
]
