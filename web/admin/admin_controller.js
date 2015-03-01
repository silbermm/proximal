module.exports = function($log,$cookieStore, standardsService, $modal){
  var vm = this; 
  vm.page = "Admin Page";
 
  vm.createStandard = function() {
    modalInstance = $modal.open({
      templateUrl: 'standards/add_standard.html',
      controller: 'AddStandardCtrl'
    });

    var addStandard = function(s) {
      standardsService.addStandard(s).success(function(data,success,headers,config) {
        $log.debug(data); 
      }).error(function(data,status,headers,config) {
        $log.error("Unable to add standard: " + data);
      });
    };

    modalInstance.result.then(function(standard){
      addStandard(standard);
    });
  };
};
