"use strict()";
(function(){

	module.exports = AddActivity;

  AddActivity.$inject = [
    "$log",
    "$modalInstance",
    "standardsService",
    "Child",
    "Question",
    "personService",
    "prox.common",
    "$stateParams"
  ];
    
  function AddActivity($log, $modalInstance, Standards, Child, Question, Person, Common, $stateParams){
		var vm = this;
    vm.addStatement = addStatement; 
    vm.availableStandards = [];
    vm.cancel = cancel;
    vm.entity = { statementIds: [] , activity: {}, resource: {}};
    vm.forms = null;
    vm.goTo = goTo;
    vm.isNextDisabled = isNextDisabled;
    vm.loadingStatements = false;
    vm.nextStep = nextStep;
    vm.ok = ok;
    vm.resourceFilter = "Questions";
    vm.selectedResource = null;
    vm.standardSelected = null;
    vm.setForm = setForm;
		vm.standard = null;
    vm.statements = [];
		
    vm.steps = [
      {step: 1, title: "Choose Standard", completed: false, selected: true },
			{step: 2, title: "Choose Statement", completed: false, selected: false},
			{step: 3, title: "General Information", completed: false, selected: false},
      {step: 4, title: "Resources", completed: false, selected: false}
		];

    function activate(){ 
      getAvailableStandards();
      getProfile();
    }

    activate();

    ///////////////////////////////////

		function setForm(f){
			vm.forms = f;
		}

    function getAvailableStandards(){
      Standards.getAllStandards().success(function(data){
			  vm.availableStandards = data;
		  }).error(function(data){
			  $log.error(data);
		  });
    }

    function getProfile(){
      vm.profile = Person.profile.get();
    }

    function getStatements(){
      vm.loadingStatements = true;
      //Get statements for this standard and grade level of the child
      Standards.getStatements(vm.standard.id).success(function(d){
        vm.loadingStatements = false;
        vm.statements = d.statements;
        //vm.statements = _.filter(d.statements, function(st){
        //return (_.contains(st.levels, vm.child.educationLevel.id) ) || (st.levels.length === 0);
        //});
      }).error(function(d){
        vm.loadingStatements = false;
        $log.error(d);
      });
    }

    function getResources(){
      vm.questionResources = Question.query(); 
    }
		
		function nextStep(){
			// which step are we on?
			var currentStep = _.find(vm.steps, function(s){
				return s.selected === true;
			});
      console.log(currentStep);
      if(currentStep.step === 1){
        getStatements(); 
      }
      if(currentStep.step === 3){
        getResources();
      }
			vm.steps[currentStep.step -1].completed = true;
			vm.steps[currentStep.step -1].selected = false;
			vm.steps[currentStep.step].selected = true;
			return true;
		}

	  function isNextDisabled(){
			return (
        vm.standard === null ||
        vm.steps[1].selected && vm.statement === undefined) || 
        (vm.steps[2].selected && !isEntityValid());
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
      var resource = Common.getResource();
			vm.entity.activity.date = new Date().getTime();      
		  vm.entity.activity.creator = vm.profile.user.uid;
      vm.entity.activity.resourceId = resource !== undefined ? resource.id : undefined;  
      $modalInstance.close(vm.entity);
		}

		function cancel() {
			$modalInstance.dismiss('cancel');
		}
	}

})();
