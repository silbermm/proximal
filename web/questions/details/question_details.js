'use strict()';
(function(){
  module.exports = function($log, $state,common){
    return {
      restrict: "E",
      scope: true,
      replace: true,
      controller: "QuestionsCtrl as ctrl",
      templateUrl: "details/detail_question.html"
    };
  };
})();
