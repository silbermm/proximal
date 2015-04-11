(function(){

  module.exports = StandardViewDirective;

  StandardViewDirective.$inject = ['standardsService'];

  function StandardViewDirective(standardsService){
    return {
      restrict: 'E',
      templateUrl: 'standards/standardView.html',
      scope: {
        activityId: '=activityId'
      },
      link: link
    };

    function link(scope, el, attr){
    
      scope.statement = {};

      activate();
      ////////////////////
      function activate(){
        // retrieve the standards for the specified activity 
        standardsService.getStatementsByActivity(scope.activityId).success(function(d){
          scope.statement = d[0];
        }).error(function(d){
          console.error(d);
        });
      }
    }
  }

})();
