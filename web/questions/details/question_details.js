'use strict()';
(function(){

  module.exports=QuestionTableDirective;
    
  function QuestionTableDirective($log, $state,common){
    return {
      restrict: "E",
      scope: {
        questions: "=questionList",
        selectable: "@selectable",
        hidePicture: "@hidePicture"
      },
      replace: true,
      //controller: "QuestionsCtrl as ctrl",
      templateUrl: "details/detail_question.html",
      link: link
    };

  
    
    function link(scope, el, attr){

      scope.hidePicture = scope.hidePicture === undefined ? false : scope.hidePicture;
      scope.isMultipleSelect = isMultipleSelect;
      scope.isSingleSelect = isSingleSelect;
   
      activate();
      ////////////////////////////
      function activate(){

      }

      function isMultipleSelect(){
        return scope.selectable === "multiple";
      }

      function isSingleSelect(){
        return scope.selectable === "single";
      }
    } 
  }


})();
