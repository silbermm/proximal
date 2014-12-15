angular.module("proximal").controller "ChildrenCtrl", [
  "$log"
  "Child"
  "$modal"
  ($log,Child, $modal) ->
    _this = this 
    
    _this.page = "Children Page" 
    _this.children = []
    _this.children = Child.query() 
    
    _this.removeChild = (id)->
      toRemove = _.findIndex(_this.children, (chr)-> 
        chr.id is id
      )
      child = new Child(toRemove)
      child.remove()
      _this.children.splice(toRemove,1) 
      
    _this.createChild = ->
      modalInstance = $modal.open({
        templateUrl: '../assets/javascripts/children/add_child.html',
        controller: 'AddChildCtrl'
      })

      addChild = (child) ->
        $log.info "adding"
        $log.info child 
        child.$save()
        console.log(child)
        _this.children.push(child)
        #personService.addChild(child).success((data,status,headers,config)->
        #  $log.info("success!")
        #  $log.info(data)
        #  $scope.children.push(data.child)
        #).error((data,status,headers,config)->
        #  $log.error(status)
        #  $log.error(data)
        #)
        return

      modalInstance.result.then ((c)->
        addChild(c)
        return
      )
]
.controller "AddChildCtrl",[
  "$scope"
  "$log"
  "$modalInstance"
  "Child"
  "prox.common"
  ($scope, $log, $modalInstance,Child,common) ->
 
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
      child = new Child({'firstName': $scope.child.firstName,'lastName': $scope.child.lastName,'birthDate': d, "educationLevel": $scope.child.gradeLevel})
      $modalInstance.close(child)

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

    $scope.tabs = [
      { title:'Dynamic Title 1', content:'Dynamic content 1' },
      { title:'Dynamic Title 2', content:'Dynamic content 2', disabled: true }
    ]

    $scope.alertMe = ()->
      setTimeout(()->
        $window.alert('You\'ve selected the alert tab!')
      )
    
    personService.getChild($stateParams.id).success((data,status,headers,config) ->
      $scope.child = data
    ).error((data,status,headers,config) ->
      $log "error!" 
    )
    return
]

