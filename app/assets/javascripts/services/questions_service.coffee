angular.module("proximal").factory "questionsService", [
  "$log"
  "$resource"
  "$http"
  ($log,$resource,$http) ->
    Question = $resource('/api/v1/questions/:id')
    return {
      question: (q)->
        return new Question(q)

      addQuestion: (q) ->
        return $http.post("/api/v1/questions", q)

      getQuestions: ->
        return Question.query()

      getQuestion: (id)->
        return Question.get({'id': id})
    }
]
