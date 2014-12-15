ChildPicture = ($log)->
  restrict: "A"
  scope: {
    child: '=child'
  }
  controller: "ChildrenCtrl"
  link: (scope,el,attr)->
    if scope.child.picture
      $log.debug("Add a picture!") 
    else
      el.css("background-image": "url(/assets/images/boy.png)")
    el.css("background-size": "contain")
    el.css("background-repeat": "no-repeat")
    if(!_.isUndefined(attr.picturePadding))
      el.css("padding-bottom": attr.picturePadding)

angular.module("proximal").directive "childPicture", ["$log", ChildPicture]
