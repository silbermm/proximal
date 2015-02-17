(function(){

  function AssesmentController($scope,$log,Child,Assesments, standardsService, $stateParams){
    var _this = this;
    _this.child = Child.get({"id": $stateParams.id});

    standardsService.getAllStandards().success(function(data){
      _this.availableStandards = data;
    }).error(function(data){
      $log.error(data);
    });

    _this.begin = function(){
      var question = Assesments.save({"childId": Number($stateParams.id), "standardId": $scope.standardSelected.id});
      var modalInstance = $modal.open({
          templateUrl: "../assets/javascripts/children/view/assesment/new/new_assesment.html", 
          controller: 'NewAssessmentCtrl',
          controllerAs: 'newAssessment',
          backdrop: false	
        });
    };
  } 
  angular.module('proximal').controller("AssesmentCtrl",['$scope','$log','Child', 'Assesments', 'standardsService', '$stateParams', AssesmentController]);
})();
