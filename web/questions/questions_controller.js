(function(){

  module.exports = QuestionController;

  QuestionController.$inject = ['$log', '$modal', 'toaster', 'Question', 'Resources'];

  function QuestionController($log, $modal, toaster,Question, Resources) {
    var vm = this;
    vm.questions = [];
    vm.addQuestion = addQuestion;

    activate();
    //////////////////////////
    
    function activate(){
      $log.debug("Getting questions!");
      vm.questions = Question.query();
    }
   
    function addQuestion(){
      var modalInstance = $modal.open({
        templateUrl: "add/add_question.html",
        controller: "AddQuestionCtrl"
      });
      modalInstance.result.then(function(obj){
        console.log(obj.resource);
        var r = new Resources(obj.resource);
        r.$save(function(res){
          obj.question.resourceId = res.id;
          var q = new Question(obj.question);
          q.$save(function(ques,headers) {
          vm.questions.push(new Question(ques));
            toaster.pop('success', "Success", "Added the question with ID " + ques.id);
          },function(err){
            toaster.pop('error', "Failure", "Unable to add the question" + err);
          }); 
        });
      });
    }
  }

})();
