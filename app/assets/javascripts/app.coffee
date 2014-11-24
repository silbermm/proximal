angular.module 'proximal', ["ui.router","ui.router.state","ngAnimate",'ngCookies', 'ngSanitize', 'ui.bootstrap', 'ui.select', 'ui.grid', 'ui.grid.selection', 'toaster', 'naif.base64']
.run ($rootScope, $state, $log) ->

  $rootScope.$on('$stateChangeStart', (event, toState, toParams, fromState, fromParams) ->
    $rootScope.hideStandards = toState.data.hideStandards
  )
.controller 'AppCtrl', ($scope, $state, $log, $cookieStore) ->
  $scope.state = $state
  mobileView = 992

  $scope.getWidth = () ->
    window.innerWidth

  $scope.$watch $scope.getWidth, (newValue, oldValue) ->
   $log.debug "watch has fired"
   if newValue >= mobileView
     if angular.isDefined($cookieStore.get("toggle"))
       if $cookieStore.get("toggle") is false
         $scope.toggle = false
       else
         $scope.toggle = true
     else
       $scope.toggle = true
   else
     $scope.toggle = false
     return

  $scope.toggleSidebar = ()->
    $log.debug "toggle called"
    $scope.toggle = !$scope.toggle
    $cookieStore.put('toggle', $scope.toggle)

  window.onresize = ()->
    $scope.$apply()
