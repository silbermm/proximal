angular.module("proximal").factory "standardsService", [
  "$log"
  "$http"
  ($log, $http) ->
    return {
      addStandard: (c) ->
        return $http.post("/api/v1/standards", c)
      
      updateStandard: (id, c) ->
        return $http.put("/api/v1/standards/" + id, c)

      getAllStandards: ->
        return $http.get("/api/v1/standards")

      removeStandard: (id)->
        return $http.delete("/api/v1/standards/" + id)

      getStandard: (id)->
        return $http.get("/api/v1/standards/" + id)

      addStatement: (standardId, statement)->
        return $http.post("api/v1/standards/" + standardId + "/statements", statement)

      getStatements: (standardId) ->
        return $http.get("api/v1/standards/" + standardId + "/statements")
    }
]
