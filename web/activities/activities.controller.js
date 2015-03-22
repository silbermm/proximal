'use strict()';
(function(){
  
  module.exports = ActivitiesController;
  
  ActivitiesController.$inject = ['$log', 'toaster', '$modal', 'Activities', 'standardsService']; 
  
  function ActivitiesController($log, toaster, $modal, Activities, Standards) {
    var vm = this;

    vm.activities = []; // holds the current activities we are looking at
    vm.begin = begin;
    vm.deleteSelectedActivities = deleteSelectedActivities;
    vm.isMyTab = isMyTab;
    vm.selectedActivities = [];
    vm.setCurrentActivities = setCurrentActivities; 
    vm.tab = "my";
    vm.updateSelection = updateSelection;
   
    activate();

    ////////////////////////////
    // Implementation Details //
    ////////////////////////////
 
    function activate(){
      getActivities();
      getAll();
      setCurrentActivities("my");
    }
 
    function addActivitySelection(activity){
      vm.selectedActivities.push(activity);
    }

    function isMyTab(){
      console.log(vm.tab === "my");
      return vm.tab === "my";
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
        backdrop: false
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
      vm.myActivities = Activities.data.query();
    }

    function getAll(){
      vm.allActivities = Activities.all.query();
    }

    function setCurrentActivities(currentTab){
      if(currentTab == "my"){
        vm.activities = vm.myActivities;
      }
      if(currentTab == "all"){
        vm.activities = vm.allActivities;
      }
      vm.tab = currentTab;
    }

  }
  

})();
