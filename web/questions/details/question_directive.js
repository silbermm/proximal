'use strict()';
(function(){
  module.exports = function(){
    return {
      restrict: "EA",
      scope:true,
      template: "<div> </div>",
      controller: "QuestionsCtrl",
      link: function(scope, elem, attr){
        outerDiv = "<div class='row col-md-12'> </div>";
        innerDiv = '<div class="col-md-4"> </div>';
        outerDiv.append(innerDiv);
        elem.append(outerDiv);
      }
    };
  };
})();
