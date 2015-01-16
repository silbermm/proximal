ChildPicture = ($log)->
  restrict: "A"
  scope: {
    child: '=child'
  }
  link: (scope,el,attr)->
    $log.debug("in the child picture directive")
    
    LoadPhoto = (ch)-> 
      el.css("background-image": "url(/assets/images/boy.png)") 
      el.css("background-size": "contain")
      el.css("background-repeat": "no-repeat")
      
      if(!_.isUndefined(attr.picturePadding))
        el.css("padding-bottom": attr.picturePadding)

    if(scope.child is undefined)
      scope.$watch("child", (newVal) ->
        LoadPhoto(newVal)  
      )
    else
      LoadPhoto(scope.child)

angular.module("proximal").directive "childPicture", ["$log", ChildPicture]
