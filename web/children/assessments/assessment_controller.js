'use strict()';


(function(){
  
  require('../../shared/standards_service');
 
  module.exports = function AssesmentController($log, standardsService, Assesments, $stateParams, $modal){
    var _this = this; 
    
    standardsService.getAllStandards().success(function(data){
      _this.availableStandards = data;
    }).error(function(data){
      $log.error(data);
    });

    _this.begin = function(){
      Assesments.save({"childId": Number($stateParams.id), "standardId": _this.standardSelected.id}, function(d){
        var modalInstance = $modal.open({
          templateUrl: "new/new_assessment.html", 
          controller: 'NewAssessmentCtrl',
          controllerAs: 'newAssessment',
          backdrop: false,
          size: 'lg',
          resolve: {
            items: function () {
              return { childId: Number($stateParams.id), "assessment": d.assessment, "question": d.question, "standardId": _this.standardSelected.id, 'picture': d.picture };
            }
          }
        });
        modalInstance.result.then(function (selectedItem) {
          $log.debug(selectedItem);
        }, function () {
          $log.info('Modal dismissed at: ' + new Date());
        }); 
      }, function(err){
        $log.error(err);  
      });    
    };
  }; 
})();
