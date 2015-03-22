'use strict()';
(function(){

  module.exports = function($log,$scope,common,$upload,standardsService,$modalInstance){
    
    $scope.select2 = {};
    $scope.uploaded = [];
    $scope.progressPercentage = 0;
    
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
    $scope.availableEducationLevels = common.educationLevels;

    $scope.getStatements = function(){
      $scope.select2.statements = [];
      standardsService.getStatements($scope.standardSelected.id).success(function(data){
        $scope.availableStatements = data.statements;
      }).error(function(data){
        $log.error("unable to get statements");
      });
    };

    $scope.educationLevelChange = function(){
      if(_.isUndefined($scope.select2.educationLevels) || _.isNull($scope.select2.educationLevels) || _.isEmpty($scope.select2.educationLevels)) {
        $scope.getStatements();
      } 
      $scope.availableStatements = _.filter($scope.availableStatements, function(s) {
        var test = _.filter(s.levels, function(l) {
          var found = _.find($scope.select2.educationLevels, function(e) {
            return l.value === e.value ;
          });
          return !_.isUndefined(found);
        });
        return test.length > 0;
      }); 
    };

    $scope.showEducationLevels = function(){
      return (!_.isUndefined($scope.standardSelected));
    };

    $scope.showStatements = function(){
      return !_.isUndefined($scope.availableStatements);
    };

    $scope.ok = function(){
      $scope.question.pictures = (angular.isDefined($scope.uploaded)) ? $scope.uploaded : null;
      $scope.question.statements = [];
      $scope.question.statements = _.map($scope.select2.statements, function(st){
        return st.statement;
      });
      $modalInstance.close($scope.question);
    };

    $scope.cancel = function(){
      $modalInstance.dismiss("cancel");
    };

  };
})();
