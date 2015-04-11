(function(){

  module.exports = DeleteStandardController;
 
  DeleteStandardController.$inject = ['$scope', '$modalInstance'];
 
  function DeleteStandardController($scope, $modalInstance){
    $scope.ok = function(){
      $modalInstance.close();
    };
    $scope.cancel = function(){
      $modalInstance.dismiss('cancel'); 
    };
  }
})();
