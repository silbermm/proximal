angular.module("proximal").factory "questionsService", [
  "$log"
  "$http"
  ($log,$http) ->
    return {
      addQuestion: (q) ->
        return $http.post("/api/v1/questions", q)

      getQuestions: ->
        return $http.get("/api/v1/questions")

      getQuestion: (id)->
        return $http.get("/api/v1/questions/" + id)
    }
]
