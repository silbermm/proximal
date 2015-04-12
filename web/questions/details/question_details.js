'use strict()';
(function(){

  module.exports=QuestionTableDirective;
    
  function QuestionTableDirective($log, $state,common){
    return {
      restrict: "E",
      scope: {
        questions: "=questionList",
        selectlist: "=selectlist",
        selectable: "@selectable",
        hidePicture: "@hidePicture"
      },
      replace: true,
      //controller: "QuestionsCtrl as ctrl",
      templateUrl: "details/detail_question.html",
      link: link
    };

  
    
    function link(scope, el, attr){
      scope.toggleResource = toggleResource;
      scope.hidePicture = scope.hidePicture === undefined ? false : scope.hidePicture;
      scope.isMultipleSelect = isMultipleSelect;
      scope.isSingleSelect = isSingleSelect;
      scope.selected = []; 
      scope.selectedResource = selectedResource;
      scope.toggleSelectAll = toggleSelectAll;

      activate();
      ////////////////////////////
      function activate(){

      }

      function toggleResource(resource){
        if(resource.selected){
          resource.selected = false;
          scope.selectlist = _.filter(scope.selectlist, function(l){
            return l.id !== resource.id;
          });
        } else {
          if(isMultipleSelect()){
            scope.selectlist.push(resource);
          } else {
            scope.selected.resource = [resource];
            _.each(scope.questions, function(r){
              r.selected = false;
            });
            common.setResource(resource);
          }
          resource.selected = true;
        }
      }

      function isMultipleSelect(){
        return scope.selectable === "multiple";
      }

      function isSingleSelect(){
        return scope.selectable === "single";
      }

      function selectedResource(resourceId){
        var sel = _.find(scope.selectlist, function(s){
          return s.id === resourceId; 
        });
        return sel !== undefined;
      }

      function toggleSelectAll(){
        if(scope.selectAll){
          _.each(scope.questions, function(question){
            scope.selectlist.push(question);
            question.selected = true;    
          });
        } else {
          _.each(scope.questions, function(question){
            // remove the question from the list
            scope.selectlist = _.filter(scope.selectlist, function(l){
              return l.id !== question.id ;
            });
            question.selected = false;    
          });
        }
      }
    } 
  }


})();
