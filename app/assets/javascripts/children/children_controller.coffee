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
        templateUrl: '../assets/javascripts/children/add/add_child.html',
        controller: 'AddChildCtrl'
      })

      addChild = (child) ->
        $log.info "adding"
        $log.info child 
        child.$save()
        console.log(child)
        _this.children.push(child)
        return

      modalInstance.result.then ((c)->
        addChild(c)
        return
      )
]
