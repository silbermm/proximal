"use strict()";
(function(){

  module.exports = EditActivity;

  EditActivity.$inject = ["$modalInstance", "standardsService", 'items'];

  function EditActivity($modalInstance, standardsService, items){
    var vm = this;
    vm.activity = items;
    vm.ok = ok;
    vm.cancel = cancel;

    activate();
    ////////////////////

    function activate(){
      console.log(vm.activity);
    }

    function ok(){
      $modalInstance.close({});
    }

    function cancel(){
      $modalInstance.dismiss('cancel');
    }
    
  }


})();
