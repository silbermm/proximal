'use strict()';
(function(){

  module.exports = function($log, common){
    return { 
      restrict: "E",
      scope: {},
      replace: true,
      templateUrl: "details/add_question.html",
      controller: "AddQuestionCtrl" 
    };
  };
})();
