InnerPageController = ($log,$scope, $element)->
  $scope.innerPageWidth
  $scope.elementId
  $scope.isOpened = false

  this.watch = (elem)->
    $log.debug("going to watch the opened variable")
    $scope.$watch("isOpened", (newVal)->
      $log.debug("isOpened changed")
      if newVal 
        elem.css("opacity", ".5")
      else
        elem.css("opacity", "1")
    )

  this.closeInnerPage = ->
    $(".left-inner-page").css("width", "0%")
    $scope.isOpened = false

  this.openInnerPage = ->
    e = $element.find(".left-inner-page")
    e.css("width", "0%")
    e.css("width", $scope.innerPageWidth) 
    $scope.isOpened = true

  this.getInnerPageWidth = ->
    return $scope.innerPageWidth
  
  this.isOpen = ->
    return $scope.isOpened

InnerPage = ($log)->
  restrict: "E"
  scope: {
    itemId : "=itemId" 
  }
  transclude: true
  replace: true
  controller: "InnerPageCtrl"
  template: "<div ng-transclude> </div>"
  link: (scope,el,attr,ctrl)->
    scope.innerPageWidth = attr.innerPageWidth  
    $log.debug("itemId = " + scope.itemId)
    if !_.isUndefined(scope.itemId)
      ctrl.openInnerPage() 
    else 
      $log.debug("close the innerpage")
       
InnerPageParent = ($log)->
  restrict: "A"
  require: "^innerPage"
  transclude: true
  template : "<div ng-transclude></div>"
  link: (scope, el, attr, ctrl)->
    ctrl.watch(el) 
InnerPageElement = ($log)->
  restrict: "E"
  require: "^innerPage"
  transclude: true
  template : "<div class='left-inner-page' ng-transclude> </div>" 
  link: (scope, el, attr,ctrl)->
    scope.elementId = el.attr('id')
    $(document).click((event)->
      if !$(event.target).closest('#' + scope.elementId).length
        if ctrl.isOpen() 
          ctrl.closeInnerPage()
    )
OpenInnerPage = ($log)->
  restrict: "A"
  require: "^innerPage"
  link: (scope,el,attr, ctrl)->
    el.bind("click", (event)->
      ctrl.openInnerPage()
      event.stopPropagation()
    )
CloseInnerPage = ($log)->
  restrict: "A"
  require: "^innerPage"  
  controller: "InnerPageCtrl"
  link: (scope,el,attr,ctrl)->
    el.bind("click", ()->
      ctrl.closeInnerPage()
    )

angular.module("proximal").controller "InnerPageCtrl",["$log","$scope", "$element", InnerPageController]
angular.module("proximal").directive "innerPage",["$log",InnerPage]
angular.module("proximal").directive "innerPageElement", ["$log",InnerPageElement]
angular.module("proximal").directive "innerPageParent", ["$log",InnerPageParent]
angular.module("proximal").directive "openInnerPage", ["$log",OpenInnerPage]
angular.module("proximal").directive "closeInnerPage", ["$log",CloseInnerPage]
