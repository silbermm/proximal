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

      scope.addResource = addResource;
      scope.hidePicture = scope.hidePicture === undefined ? false : scope.hidePicture;
      scope.isMultipleSelect = isMultipleSelect;
      scope.isSingleSelect = isSingleSelect;
      scope.selected = []; 
      scope.selectedResource = selectedResource;

      activate();
      ////////////////////////////
      function activate(){

      }

      function addResource(resource){
        if(isMultipleSelect()){
          scope.selected.push(resource);
        } else {
          scope.selected.resource = [resource];
          _.each(scope.questions, function(r){
            r.selected = "";
          });
          common.setResource(resource);
        }
        resource.selected = "success";
      }

      function isMultipleSelect(){
        return scope.selectable === "multiple";
      }

      function isSingleSelect(){
        return scope.selectable === "single";
      }

      function selectedResource(resourceId){
        var sel = _.find(scope.selected, function(s){
          console.log("scope.selected = " + scope.selected);
          console.log("resourceId = " + resourceId);
          return s.id === resourceId; 
        });
        console.log(sel);
        return sel !== undefined;
      }
    } 
  }


})();
