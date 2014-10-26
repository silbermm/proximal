angular.module("proximal").factory "personService", [
  "$log"
  "$http"
  ($log, $http) ->
    return {
      addChild: (c) ->
        return $http.post("/api/v1/children/add", c)
          
      getChildren: ->
        return $http.get("/api/v1/children")

      removeChild: (id)->
        return $http.delete("/api/v1/children/" + id)

      getChild: (id)->
        return $http.get("/api/v1/children/" + id)
    }
]
