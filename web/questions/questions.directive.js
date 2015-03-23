(function(){

  module.exports = QuestionsDirective;

  function QuestionsDirective(){
    return {
      restrict: "EA",
      controller: "QuestionsCtrl as ctrl",
      templateUrl: 'questions/questions.html',
      scope: true
    };
  }
})();
