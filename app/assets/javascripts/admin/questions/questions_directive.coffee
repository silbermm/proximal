QuestionDirective = ->
  restrict: "EA"
  scope:true
  template: "<div> </div>"
  controller: "QuestionsCtrl"
  link: (scope, elem, attr) ->
    outerDiv = $('<div>', {class: "row col-md-12"})
    innerDiv = $('<div>', {class: "col-md-4"})
    outerDiv.append(innerDiv)
    elem.append(outerDiv)

QuestionPictureDirective = ($log,$q)->
  restrict: "A"
  scope: true
  controller: "QuestionsCtrl"
  link: (scope,elem,attr) -> 
    $log.debug(scope.question)
    scope.$watch "question", ((newVal) ->
      if newVal
        if scope.question.picture
          elem.css("background-image": "url(data:image/png;base64," + scope.question.picture) 
        else
          elem.css("background-image": "url(/assets/images/emptyImage.png)") 
      return
    ), true
    elem.css("background-size": "cover")
    elem.css("background-repeat": "no-repeat")

angular.module("proximal").directive "question", QuestionDirective
angular.module("proximal").directive "questionPicture", ['$log', '$q', QuestionPictureDirective]
