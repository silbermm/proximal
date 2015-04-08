'use strict()';
(function(){
  module.exports = function ($log,$scope,$modalInstance,common,standardsService,standardId){
      $scope.availableEducationLevels = common.educationLevels;
      $scope.edu = {
        statement: {}
      };
      $scope.edu.levels = {};

      standardsService.getStandard(standardId).success(function(data) {
        $scope.edu.statement.subject = data.standard.subject;
        /*$scope.availableEducationLevels = _.filter($scope.availableEducationLevels, function(lev) {*/
          //var has = _.find(data.levels,function(standardLevel){
            //return standardLevel.description === lev.description;
          //});
          //return has === undefined;
        /*});*/
      }).error(function(data){
        $log.error(data);
      });
      
      $scope.ok = function(){
        $modalInstance.close($scope.edu);
      };

      $scope.cancel = function(){
        $modalInstance.dismiss("cancel");
      };
    };
})();
