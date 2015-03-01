'use strict()';
(function(){

module.exports = function ($log, $scope, $modalInstance, common,toaster){

    $scope.edu = {};
    $scope.clear = function(){
      $scope.standard.dateValid = null;
    }; 

    $scope.dateValidOpen = function($event){
      $event.preventDefault();
      $event.stopPropagation();
      $scope.dateValidOpened = true;
    };
      
    $scope.repoDateOpen = function($event){
      $event.preventDefault();
      $event.stopPropagation();
      $scope.repoDateOpened = true;
    }; 

    $scope.dateOptions = {
      formatYear: 'yyyy',
      startingDay: 1
    };

    $scope.initDate = new Date("Jan 1, 2008");
   
    $scope.format = 'mediumDate';

    $scope.subjects = [
      "math",
      "english",
      "science" 
    ];

    $scope.languages = [
      "English",
      "Spanish",
      "French"
    ];
    
    $scope.availableEducationLevels = common.educationLevels;
    
    $scope.ok =function(){
      $modalInstance.close($scope.edu);
    };

    $scope.cancel = function(){
      $modalInstance.dismiss("cancel");
    };
  };
})();

