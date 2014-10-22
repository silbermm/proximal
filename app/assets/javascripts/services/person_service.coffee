angular.module("proximal").factory "personService", [
  "$log"
  "$http"
  ($log, $http) ->
    return {
      addChild: (rootUrl, c) ->
        return $http.post(rootUrl + "/api/v1/children/add", c)
          
      getChildren: (rootUrl) ->
        return $http.get(rootUrl + "/api/v1/children")
    }
]
