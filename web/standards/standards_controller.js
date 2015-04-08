'use strict()';

(function(){
  module.exports = function($log,$scope, $state, $stateParams,$modal,standardsService, toaster){

    var initWithId = function(){
      standardsService.getStandard($stateParams.id).success(function(data,status,headers,config){
        $scope.standard = data.standard;
        $scope.educationLevels = data.levels;
      }).error(function(data,status,headers,config){
        toaster.pop('error', "Failure", "Unable to get standareds");
      });
      standardsService.getStatements($stateParams.id).success(function(data){
        $scope.statements = data.statements;
      }).error(function(data,status){
        $log.error(data);
      });
    };

    var init = function(){
      standardsService.getAllStandards().success(function(data,status){
        $scope.standards = data;
      }).error(function(data,status){
        toaster.pop('error', "Failure", "Unable to get standards");
      });
    };


    $scope.deleteStandard = function(){
      var modalInstance = $modal.open({
        templateUrl: "delete/delete_standard.html",
        controller: "DeleteStandardController"
      });
      modalInstance.result.then( function(){
        standardsService.removeStandard($scope.standard.id).success(function(data){
          toaster.pop("success", null, "Successfully deleted standard") ;
          $scope.standards = _.filter($scope.standards, function(st){
            return st.id !== $scope.standard.id;
          });
          $state.go("admin.standards");
        }).error(function(data){
          $log.debug("Error: ");
          $log.debug(data);
        });
      },function(){
        $log.info("dismissed delete modal");
      });
    };
 
    $scope.isDescriptionCollapsed = false;
    $scope.isEducationCollapsed = false;
   
    $scope.addStatement = function(){
      var modalInstance = $modal.open({
        templateUrl: "add/add_statement.html",
        controller: "AddStatementCtrl",
        resolve: {
          standardId: function(){
            return $stateParams.id;
          }
        }
      });

      modalInstance.result.then(function(statement){
        standardsService.addStatement($stateParams.id, statement).success(function(data,status,headers,config){
          $scope.statements.push(data);
          toaster.pop('success', null, "Successfully added the statement");
        }).error(function(data,status,headers,config){
          $log.error(data);
          $log.error(status);
          toaster.pop('error',null, "Failed to add the statement: " + data.message);
        });
      });
    };

    $scope.addStandard = function(){
      var modalInstance = $modal.open({
        templateUrl: 'add/add_standard.html',
        controller: 'AddStandardCtrl'
      });

      var addStandard = function(s){
        standardsService.addStandard(s).success(function(data,success,headers,config){
          $scope.standards.push(data.standard);
          toaster.pop('success', null, "Successfully added the new standard");
        }).error(function(data,status,headers,config) {
          toaster.pop('error',null, "Failed to add the standard: " + data.message);
        });
      };
      
      modalInstance.result.then(function(standard){
        addStandard(standard);
      });
    };

    if(!_.isUndefined($stateParams.id)) {
      initWithId();
    }else{
      init();
    }
  };
})();

