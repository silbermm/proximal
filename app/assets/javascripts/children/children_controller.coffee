angular.module("proximal").controller "ChildrenCtrl",($log,$cookieStore,$scope,personService, $modal) ->
  $scope.page = "Children Page"

  $scope.children = []

  personService.getChildren().success((data, status, headers, config) ->
    $scope.children = data
  ).error((data, status, headers, config)->
    $log.error(data)
  )

  $scope.removeChild = (id)->
    toRemove = _.findIndex($scope.children, (chr)-> 
      chr.id is id
    )
    personService.removeChild(id).success((data,status,headers,config)->
      $scope.children.splice(toRemove, 1)
    ).error((data,status,headers,config)->
      $log.error("there was an error " + data)
    )

  $scope.createChild = ->
    modalInstance = $modal.open({
      templateUrl: '../assets/javascripts/children/add_child.html',
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

.controller "AddChildCtrl",[
  "$scope"
  "$log"
  "$modalInstance"
  "prox.common"
  ($scope, $log, $modalInstance,common) ->
 
    $scope.availableLevels = common.educationLevels

    $scope.clear = ->
      $scope.child.birthDate = null
      return
     
    $scope.open = ($event)->
      $event.preventDefault()
      $event.stopPropagation()
      $scope.opened = true

    $scope.dateOptions = {
      formatYear: 'yyyy',
      startingDay: 1
    }

    $scope.initDate = new Date("Jan 1, 2008")

    $log.debug($scope.initDate)
    
    $scope.format = 'mediumDate'

    $scope.ok = ->
      d = $scope.child.birthDate.getTime()/1000
      $modalInstance.close({'firstName': $scope.child.firstName,'lastName': $scope.child.lastName,'birthDate': d, "educationLevel": $scope.child.gradeLevel})

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")
]

angular.module('proximal').controller "ViewChildCtrl", [
  "$scope"
  "$log"
  "$stateParams"
  "personService"
  ($scope,$log,$stateParams, personService) ->
    $log.info($stateParams.id)
    personService.getChild($stateParams.id).success((data,status,headers,config) ->
      $scope.child = data
    ).error((data,status,headers,config) ->
      $log "error!" 
    )
    return
]

