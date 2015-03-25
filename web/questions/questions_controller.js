(function(){

  module.exports = QuestionController;

  QuestionController.$inject = ['$log', '$modal', 'toaster', 'Question', 'Resources'];

  function QuestionController($log, $modal, toaster,Question, Resources) {
    var vm = this;
    
    vm.addQuestion = addQuestion;
    vm.deleteQuestion = deleteQuestion;
    vm.selectedQuestions = []; 
    vm.questions = [];

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
            res.question = ques;
            vm.questions.push(res);
            toaster.pop('success', "Success", "Added the question with ID " + ques.id);
          },function(err){
            toaster.pop('error', "Failure", "Unable to add the question" + err);
          }); 
        });
      });
    }

    function deleteQuestion(){
      _.each(vm.selectedQuestions, function(q){
        Question.delete({id: q.question.id}, function(s){
          vm.questions = _.filter(vm.questions, function(fq){
            return fq.question.id !== q.question.id;
          });
        }); 
      });
      vm.selectedQuestions = [];
    }
  }

})();
