angular.module("proximal").directive "proxChildren",[
  '$log',
  'personService',
  ($log, personService) ->
    return {
      restrict: "E"
      replace: true
      templateUrl: "assets/javascripts/children/children_directive_template.html",
      scope: {}
      link: (scope, el, attrs) ->
        
        scope.children = [] 
       
        personService.getChildren("http://localhost:9000").success((data, status, headers, config) ->
          scope.children = data.children
        ).error((data, status, headers, config)->
          $log.error(data)
        )
 
        return
    }
]
