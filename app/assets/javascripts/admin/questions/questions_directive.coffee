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

QuestionDetailsDirective = ($log, $state,common)->
  restrict: "E"
  scope: true
  replace: true
  controller: "QuestionsCtrl as ctrl"
  templateUrl: "../assets/javascripts/admin/questions/detail_question.html"
  link : (scope,elem,attr)->
    
QuestionPictureDirective = ($log,$q)->
  restrict: "A"
  scope: true
  controller: "QuestionsCtrl"
  link: (scope,elem,attr) -> 
    scope.$watch "question", ((newVal) ->
      if newVal
        if scope.question.picture
          elem.css("background-image": "url(data:image/png;base64," + scope.question.picture) 
        else
          elem.css("background-image": "url(/assets/images/emptyImage.png)") 
      return
    ), true
    elem.css("background-size": "contain")
    elem.css("background-repeat": "no-repeat")
    console.log(attr);
    if(!_.isUndefined(attr.picturePadding))
      elem.css("padding-bottom" : attr.picturePadding)

angular.module("proximal").directive "question", QuestionDirective
angular.module("proximal").directive "questionPicture", ['$log', '$q', QuestionPictureDirective]
angular.module("proximal").directive "questionDetails", ['$log', '$state', 'prox.common' ,QuestionDetailsDirective]
