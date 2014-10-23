angular.module("proximal").controller "ChildrenCtrl",($log,$cookieStore,$scope,personService, $modal) ->
  $scope.page = "Children Page"

  $scope.children = [] 
       
  personService.getChildren().success((data, status, headers, config) ->
    $scope.children = data.children
  ).error((data, status, headers, config)->
    $log.error(data)
  )

  $scope.createChild = ->
    #open a modal
    modalInstance = $modal.open({
      templateUrl: '../assets/javascripts/children/templates/add_child.html',
      controller: 'AddChildCtrl'
    })

    addChild = (child) ->
      $log.info "adding"
      $log.info child
      personService.addChild(child).success((data,status,headers,config)->
        $log.info("success!")
        $log.info(data)
        $scope.children.push(data.child)
      ).error((data,status,headers,config)->
        $log.error(status)
        $log.error(data)
      )
      return
   
    modalInstance.result.then ((c)->
      addChild(c)
      return
    )

angular.module("proximal").controller "AddChildCtrl", ($scope, $log, $modalInstance) ->

  $scope.clear = ->
    $scope.child.birthDate = null
    return
   
  $scope.open = ($event)->
    $event.preventDefault()
    $event.stopPropagation()
    $scope.opened = true

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  }

  $scope.initDate = new Date("Jan 1, 2008")

  $log.debug($scope.initDate)

  
  $scope.format = 'mediumDate'

  $scope.ok = ->
    d = $scope.child.birthDate.getTime()/1000
    $modalInstance.close({'firstName': $scope.child.firstName,'lastName': $scope.child.lastName,'birthDate': d})

  $scope.cancel = ->
    $modalInstance.dismiss("cancel")
