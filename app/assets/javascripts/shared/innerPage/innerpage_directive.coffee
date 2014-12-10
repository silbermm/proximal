InnerPageController = ($log,$scope, $element)->
  $scope.innerPageWidth

  this.closeInnerPage = ->
    $log.debug("called closeInnerPage!")
    $(".left-inner-page").css("width", "0%")

  this.openInnerPage = ->
    e = $element.find(".left-inner-page")
    e.css("width", "0%")
    e.css("width", $scope.innerPageWidth)

  this.getInnerPageWidth = ->
    return $scope.innerPageWidth

InnerPage = ($log)->
  restrict: "E"
  scope: {}
  transclude: true
  replace: true
  controller: "InnerPageCtrl"
  template: "<div ng-transclude> </div>"
  link: (scope,el,attr)->
    scope.innerPageWidth = attr.innerPageWidth
    el.bind("click", ()->
      $log.debug("clicked outside the innerwindow!")
    )
    
InnerPageElement = ($log)->
  restrict: "E"
  require: "^innerPage"
  transclude: true
  template : "<div class='left-inner-page' ng-transclude> </div>" 
  
OpenInnerPage = ($log)->
  restrict: "A"
  require: "^innerPage"
  link: (scope,el,attr, ctrl)->
    el.bind("click", ()->
      ctrl.openInnerPage()
    )
CloseInnerPage = ($log)->
  restrict: "A"
  controller: "InnerPageCtrl"
  link: (scope,el,attr,ctrl)->
    $log.debug(attr) 
    el.bind("click", ()->
      ctrl.closeInnerPage()
    )

angular.module("proximal").controller "InnerPageCtrl",["$log","$scope", "$element", InnerPageController]
angular.module("proximal").directive "innerPage",["$log",InnerPage]
angular.module("proximal").directive "innerPageElement", ["$log",InnerPageElement]
angular.module("proximal").directive "openInnerPage", ["$log",OpenInnerPage]
angular.module("proximal").directive "closeInnerPage", ["$log",CloseInnerPage]
