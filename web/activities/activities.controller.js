'use strict()';
(function(){
  
  module.exports = ActivitiesController;
  
  ActivitiesController.$inject = ['$log', 'toaster', '$modal', 'Activities', 'standardsService']; 
  
  function ActivitiesController($log, toaster, $modal, Activities, Standards) {
    var vm = this;

    vm.activities = [];
    vm.availableStandards = [];
    vm.begin = begin;
    vm.deleteSelectedActivities = deleteSelectedActivities;
    vm.selectedActivities = [];
    vm.standardSelected = null;
    vm.updateSelection = updateSelection;

    function activate(){
      getActivities();
      getAvailableStandards();
    }
   
    activate();

    ////////////////////////////
    // Implementation Details //
    ////////////////////////////
  
    function addActivitySelection(activity){
      vm.selectedActivities.push(activity);
    }

    function removeActivitySelection(activity){
      vm.selectedActivities = _.remove(vm.selectedActivities, function(a){
        return a.id !== activity.id;
      });
    }

    function updateSelection(activity,selected) {
      if(selected) {
        addActivitySelection(activity);
      } else {
        removeActivitySelection(activity);
      }
    }

    function deleteSelectedActivities(){
      _.each(vm.selectedActivities, function(a){
        a.$delete({activityId: a.id}); 
      });
    }

    function begin(){
      var modalInstance = $modal.open({
				templateUrl: "add/add_activity.html", 
				controller: 'AddActivityController',
				controllerAs: 'add',
        backdrop: false,
				resolve: { items: function(){ return vm.standardSelected; } }
			});

			modalInstance.result.then(function (result) {
				var activity = new Activities.data(result);
				activity.$save(function(saved){
          vm.activities.push(saved);
					toaster.pop("success", null, "Successfully added a new activitiy!");
				}, function(error){
					toaster.pop("error", null, "There was an error when trying to add the activity. Please try again.");
				});
			});
    }

    function getActivities(){
      vm.activities = Activities.data.query();
    }

    function getAvailableStandards(){
      Standards.getAllStandards().success(function(data){
			  vm.availableStandards = data;
		  }).error(function(data){
			  $log.error(data);
		  });
    }
  }


})();
