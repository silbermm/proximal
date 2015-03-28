(function(){
 
  module.exports = ResourceCompactView;

  ResourceCompactView.$inject = ['Resources', "QuestionByResource"];

  function ResourceCompactView(Resources, QuestionByResource){
    return {
      restrict: "E",
      templateUrl: "resources/resourceCompactView.html",
      scope: {
        resourceId: '='
      },
      link: link
    };

    function link(scope, el, attrs){
      
      activate();

      ///////////////
      function activate(){
        if(scope.resourceId !== undefined){
          scope.resource = Resources.get({resourceId: scope.resourceId}, function(resource){
            if(resource.category === 'question'){
              scope.question = QuestionByResource.get({resourceId: resource.id});
            }
          }); 
        } 
      }

    }
  }

})();
