(function(){
  module.exports = activityWidget;

  function activityWidget() {
    return {
      restrict: 'EA',
      templateUrl: 'activities/activityWidget.html',
      controller: 'ActivitiesController',
      bindToController: true
    };
  }
})();
