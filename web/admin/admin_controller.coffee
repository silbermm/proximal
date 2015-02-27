angular.module("proximal").controller "AdminCtrl", ($log,$cookieStore,$scope, standardsService, $modal) ->
  $scope.page = "Admin Page"
 
  $scope.createStandard = ->
    $log.debug("create a new standard")
    modalInstance = $modal.open({
      templateUrl: '../assets/javascripts/admin/standards/add_standard.html',
      controller: 'AddStandardCtrl'
    })

    addStandard = (s) ->
      standardsService.addStandard(s).success((data,success,headers,config) ->
        $log.debug data
      ).error((data,status,headers,config) ->
        $log.error("Unable to add standard: " + data)
      )

    modalInstance.result.then((standard)->
      addStandard(standard)
      return
    )
 
