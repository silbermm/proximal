'use strict()';
(function(){
  
  module.exports = ActivitiesController;
  
  ActivitiesController.$inject = ['$log', 'toaster', '$modal', 'Activities', 'standardsService']; 
  
  function ActivitiesController($log, toaster, $modal, Activities, Standards) {
    var vm = this;

    vm.activities = []; // holds the current activities we are looking at
    vm.begin = begin;
    vm.deleteSelectedActivities = deleteSelectedActivities;
    vm.editActivity = editActivity;
    vm.getResource = getResource;
    vm.isMyTab = isMyTab;
    vm.selectedActivities = [];
    vm.setCurrentActivities = setCurrentActivities; 
    vm.tab = "my";
    vm.updateSelection = updateSelection;
    vm.resource = [];
   
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

    function editActivity(activity){
      var modalInstance = $modal.open({
        templateUrl: "edit/editActivity.html", 
				controller: 'EditActivityController',
				controllerAs: 'edit',
        backdrop: false,
        resolve: {
          items: function(){
            return activity;
          }
        }
      });

      modalInstance.result.then(function(result){
        console.log(result); 
      });
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
        a.$delete({activityId: a.id}, function(){
          vm.activities = _.filter(vm.activities, function(a2){
            return a2.id !== a.id;
          });
        }); 
      });
      vm.selectedActivities = [];
    }

    
    function getActivities(){
      vm.myActivities = Activities.data.query();
    }

    function getAll(){
      vm.allActivities = Activities.all.query();
    }

    function getResource(resourceId){

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
