'use strict()';
(function(){

  module.exports = function($log,$scope,common,$upload,standardsService,$modalInstance){
    
    $scope.select2 = {};
    $scope.uploaded = [];
    $scope.progressPercentage = 0;
    
    $scope.resource = {};

    standardsService.getAllStandards().success(function(data){
      $scope.availableStandards = data;
    }).error(function(data){
      $log.error(data);
    });

    $scope.upload = function(files) {
      if(angular.isDefined(files) && files.length > 0){
        _.each(files, function(file){
          $upload.upload({
            url: 'api/v1/upload',
            file: file
          }).progress(function(evt){
            $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $log.debug($scope.progressPercentage);
          }).success(function(data,status,headers,config) {
            $scope.uploaded.push(data);
            $scope.progressPercentage = 0;
            $scope.picture = undefined;
          }).error(function(data,status,headers,config) {
            $log.error(data);
          });
        });
      }
    };

    $scope.ok = function(){
      $scope.resource.category="question";
      $scope.question.pictures = (angular.isDefined($scope.uploaded)) ? $scope.uploaded : null; 
      $modalInstance.close({'question' : $scope.question, 'resource' : $scope.resource});
    };

    $scope.cancel = function(){
      $modalInstance.dismiss("cancel");
    };

  };
})();
