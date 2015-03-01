'use strict()';
(function(){

  module.exports = function($log,$scope,$state,$stateParams,common,standardsService,Question,toaster){
      
      var GetAvailableStatments = function(){
        if(!_.isUndefined($scope.question.statements[0])){
          if(!_.isUndefined($scope.standard)){
            standardsService.getStatements($scope.standard.id).success(function(data){
              $scope.availableStatements = data.statements;
            }).error(function(data){
               $log.error("unable to retrieve statements");
            }); 
          } else {
            $scope.availableStatements = [];
          }
        }
      };
     
      Standards = standardsService.standards;
      $scope.select2 = { statements : [] };

      $scope.init = function(){
        $scope.question = Question.get({"id": $stateParams.questionId}, function(ques){
          if (_.isUndefined($scope.question.statements)){
            $scope.select2.statements = [];
          } else {
            $scope.select2.statements = $scope.question.statements;
          }
        });

        $scope.availableStandards = Standards().query();
      
        $scope.availableStandards.$promise.then(function(s){
          $scope.question.$promise.then(function(q){
            if(!_.isUndefined($scope.question.statements[0])){ 
              $scope.standard = _.find(s, function(stan){
                return stan.id === $scope.question.statements[0].standardId ;
              });
              if(!_.isUndefined($scope.standard)){
                standardsService.getStatements($scope.standard.id).success(function(data){
                  $scope.availableStatements = data.statements;
                }).error(function(data){
                   $log.error("unable to retrieve statements");
                }); 
              } else {
                $scope.availableStatements = [];
              }
            }
          });
        });
      };

      $scope.changeStandard = function(){
        if(!_.isUndefined($scope.standard)) {
          standardsService.getStatements($scope.standard.id).success(function(data){
            $scope.availableStatements = data.statements;
          }).error(function(data){
            $log.error("unable to retrieve statements");
          });
        } else {
          $scope.availableStatements = [];
        }
      };

      $scope.update = function(){
        $scope.question.$update(function(){
          toaster.pop('success', "Updated", "Successfully updated question");
        },function(){
          toaster.pop('error', "Failed", "Did not update the question, contact the administrator");
        });
      };

      $scope.delete = function(){
        Question.remove({"id": $scope.question.id}, function(q){
          toaster.pop('success', "Deleted", "Successfully removed question " + $scope.question.id);
          $state.go("admin.questions");
        }, function(){
          toaster.pop('error', "Failed", "Unable to delete this question");
        });
      };
  };
})(); 
