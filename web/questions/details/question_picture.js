(function(){

module.exports = function($log,$q){
  return {
    restrict: "A",
    scope: true,
    controller: "QuestionsCtrl",
    link: function(scope,elem,attr) {
      
      scope.$watch("question", function(newVal) {
        if(newVal){
          if(scope.question.picture){
            elem.css({"background-image": "url(data:image/png;base64," + scope.question.picture});
          } else {
            elem.css({"background-image": "url(/assets/images/emptyImage.png)"});
          }
        }
      }, true);
      
      elem.css({"background-size": "contain"});
      elem.css({"background-repeat": "no-repeat"});
      
      if(!_.isUndefined(attr.picturePadding)) { 
        elem.css({"padding-bottom" : attr.picturePadding});
      }

    } 
  };
};
})();
