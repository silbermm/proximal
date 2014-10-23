angular.module("proximal").factory "personService", [
  "$log"
  "$http"
  ($log, $http) ->
    return {
      addChild: (c) ->
        return $http.post("/api/v1/children/add", c)
          
      getChildren: ->
        return $http.get("/api/v1/children")
    }
]
