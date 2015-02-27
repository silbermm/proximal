'use strict()';

require('../../shared/person_service');

module.exports = function($log,$window,$stateParams, personService){
  var vm = this;  
  personService.getChild($stateParams.id).success(function(data,status,headers,config) {
    vm.child = data;
  }).error(function(data,status,headers,config){
      $log.error("error!");
  });
};
