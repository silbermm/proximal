"use strict()";
(function(){

	module.exports = AddActivity;

  AddActivity.$inject = [
    "$log",
    "$modalInstance",
    "standardsService",
    "Child",
    "personService",
    "prox.common",
    "$stateParams",
    "items"
  ];
    
  function AddActivity($log, $modalInstance, Standards, Child, Person, Common, $stateParams, items){
		var vm = this;
    vm.addStatement = addStatement; 
    vm.cancel = cancel;
    vm.entity = { statementIds: [] };
    vm.forms = null;
    vm.goTo = goTo;
    vm.isNextDisabled = isNextDisabled;
    vm.nextStep = nextStep;
    vm.ok = ok;
    vm.setForm = setForm;
		vm.standard = items;
    vm.statements = [];
		vm.steps = [
			{step: 1, title: "Choose Statement", completed: false, selected: true},
			{step: 2, title: "General Information", completed: false, selected: false},
      {step: 3, title: "Resources", completed: false, selected: false}
		];

    function activate(){
      getStatements();
      getProfile();
    }

    activate();

    ///////////////////////////////////

		function setForm(f){
			vm.forms = f;
		}

    function getProfile(){
      vm.profile = Person.profile.get();
    }

    function getStatements(){
      //Get statements for this standard and grade level of the child
      Standards.getStatements(vm.standard.id).success(function(d){
        vm.statements = d.statements;
        //vm.statements = _.filter(d.statements, function(st){
        //return (_.contains(st.levels, vm.child.educationLevel.id) ) || (st.levels.length === 0);
        //});
      }).error(function(d){
        $log.error(d);
      });
    }
		
		function nextStep(){
			// which step are we on?
      console.log(vm.entity);
			var currentStep = _.find(vm.steps, function(s){
				return s.selected === true;
			});
			vm.steps[currentStep.step -1].completed = true;
			vm.steps[currentStep.step -1].selected = false;
			vm.steps[currentStep.step].selected = true;
			return true;
		}

	  function isNextDisabled(){
			return (vm.steps[0].selected && vm.statement === undefined) || 
        (vm.steps[1].selected && !isEntityValid());
		}

		function goTo(step){
			_.each(vm.steps, function(s){
				if(s.step === step.step){
					s.selected = true;
				} else {
					s.selected = false;
					s.completed = false;
				}
			});
		}
  
    function isEntityValid(){
      return true;
    }
    
    function addStatement(statement){
      vm.statement = statement;
      vm.entity.statementIds = [ statement.id ];
    }

    function ok() {
			vm.entity.activity.date = new Date().getTime();      
		  vm.entity.activity.creator = vm.profile.user.uid;
      $modalInstance.close(vm.entity);
		}

		function cancel() {
			$modalInstance.dismiss('cancel');
		}
	}

})();
