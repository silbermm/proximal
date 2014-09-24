angular.module 'proximal', ["templates-main", "ui.router","ui.router.state","ngAnimate"]

.run ($rootScope, $state, $log) ->

  $rootScope.$on('$stateChangeStart', (event, toState, toParams, fromState, fromParams) ->
    $log.debug("state changed!");
    
    if angular.isDefined( toState.data.pageTitle )
      $rootScope.pageTitle = toState.data.pageTitle + ' | AlwaysUC' ;
                         

    
  );

.controller 'AppCtrl', ($scope, $state, $log) ->
  $scope.state = $state;
  $scope.copyright = new Date()
