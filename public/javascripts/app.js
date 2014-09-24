(function() {
  angular.module('proximal', ["templates-main", "ui.router", "ui.router.state", "ngAnimate"]).run(function($rootScope, $state, $log) {
    return $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
      $log.debug("state changed!");
      if (angular.isDefined(toState.data.pageTitle)) {
        return $rootScope.pageTitle = toState.data.pageTitle + ' | AlwaysUC';
      }
    });
  }).controller('AppCtrl', function($scope, $state, $log) {
    $scope.state = $state;
    return $scope.copyright = new Date();
  });

}).call(this);
