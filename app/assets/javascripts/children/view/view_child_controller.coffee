angular.module('proximal').controller "ViewChildCtrl", [
  "$log"
  "$window"
  "$stateParams"
  "personService"
  ($log,$window,$stateParams, personService) ->
    _this = this
    
    personService.getChild($stateParams.id).success((data,status,headers,config) ->
      _this.child = data
    ).error((data,status,headers,config) ->
      $log "error!" 
    )
    return
]
