(function(){

	function HomeworkCtrl($log, $modal, standardsService) {
		var _this = this;

		standardsService.getAllStandards().success(function(data){
			_this.availableStandards = data;
		}).error(function(data){
			$log.error(data);
		});

		_this.begin = function() {
			var modalInstance = $modal.open({
				templateUrl: "../assets/javascripts/children/view/homework/add/add_homework.html", 
				controller: 'AddHomeworkCtrl',
				controllerAs: 'addHomework',
				resolve: {
					items: function () {
						return _this.standardSelected;
					}
				}
			});

			modalInstance.result.then(function (result) {
				this.addedHomework = result;
			}, function () {
				$log.info('Modal dismissed at: ' + new Date());
			});
		};
	}
	angular.module('proximal').controller("HomeworkCtrl", ["$log", '$modal', 'standardsService', HomeworkCtrl]);
})();