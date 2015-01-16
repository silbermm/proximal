angular.module('proximal').controller "ViewChildCtrl", [
  "$scope"
  "$log"
  "$window"
  "$stateParams"
  "personService"
  ($scope,$log,$window,$stateParams, personService) ->
    $log.info($stateParams.id)

    $scope.tabs = [
      { title:'Dynamic Title 1', content:'Dynamic content 1' },
      { title:'Dynamic Title 2', content:'Dynamic content 2', disabled: true }
    ]

    $scope.alertMe = ()->
      setTimeout(()->
        $window.alert('You\'ve selected the alert tab!')
      )
    
    personService.getChild($stateParams.id).success((data,status,headers,config) ->
      $scope.child = data
    ).error((data,status,headers,config) ->
      $log "error!" 
    )
    return
]
