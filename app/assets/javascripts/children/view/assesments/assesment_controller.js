(function(){

  function AssesmentController($log, standardsService, Assesments, $stateParams, $modal){
    var _this = this;
    
    $log.debug("Getting available Standards!");
    standardsService.getAllStandards().success(function(data){
      _this.availableStandards = data;
    }).error(function(data){
      $log.error(data);
    });

    _this.begin = function(){
      var question = Assesments.save({"childId": Number($stateParams.id), "standardId": _this.standardSelected.id});
      var modalInstance = $modal.open({
          templateUrl: "../assets/javascripts/children/view/assesments/new/new_assesment.html", 
          controller: 'NewAssessmentCtrl',
          controllerAs: 'newAssessment',
          backdrop: false,
          size: 'lg',
          resolve: {
            items: function () {
              return question;
            }
          }
        });
      modalInstance.result.then(function (selectedItem) {
        $log.debug(selectedItem);
      }, function () {
        $log.info('Modal dismissed at: ' + new Date());
      });

    };
  } 
  angular.module('proximal').controller("AssesmentCtrl",['$log', 'standardsService', 'Assesments', '$stateParams', '$modal', AssesmentController]);
})();
