angular.module("proximal").controller "AdminCtrl", ($log,$cookieStore,$scope, standardsService, $modal) ->
  $scope.page = "Admin Page"

  standardsService.getAllStandards().success((data,status)->
    $scope.standards = data
  ).error((data,status)->
    $log.error(data)
  )

  $scope.createStandard = ->
    $log.debug("create a new standard")
    modalInstance = $modal.open({
      templateUrl: '../assets/javascripts/admin/standards/add_standard.html',
      controller: 'AddStandardCtrl'
    })

    addStandard = (standard) ->
      $log.info "adding"
      $loginfo standard
      return
    modalInstance.result.then((c)->
      addStandard(c)
      return
    )
 
