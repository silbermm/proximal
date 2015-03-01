module.exports = function ($log,$scope,$state, $stateParams, $modal, toaster,common, Question, standardsService) {

  $scope.init = function(){
    $scope.questions = Question.query();
  };

  $scope.availableEducationLevels = common.educationLevels;
  
  $scope.addQuestion = function(){
    var modalInstance = $modal.open({
      templateUrl: "add/add_question.html",
      controller: "AddQuestionCtrl"
    });
    modalInstance.result.then(function(question){
      var q = new Question(question);
      q.$save(function(ques,headers) {
        $scope.questions.push(new Question(ques));
        toaster.pop('success', "Success", "Added the question with ID " + ques.id);
      },function(err){
        toaster.pop('error', "Failure", "Unable to add the question" + err);
      });
    });
  };
};
