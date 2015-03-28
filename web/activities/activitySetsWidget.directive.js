(function(){
 
  module.exports = ActivitySetsWidget;
  
  ActivitySetsWidget.$inject = ['Activities'];

  function ActivitySetsWidget(Activities){

    return {
      restrict: "EA",
      templateUrl: "activities/activitySets.html",
      link: link
    };
    
    function link(scope, el, attrs){
     
      scope.activitySets = [];

      activate();
      /////////////////////
      function activate(){
                  
      }

      function loadData(){
        scope.activitySets = Activities.sets.query();
      }
    } 
  }

})();
