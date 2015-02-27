angular.module("proximal").controller "AddQuestionCtrl",[
  "$log"
  "$scope"
  "prox.common"
  "$upload"
  "standardsService"
  "$modalInstance"
  ($log,$scope,common,$upload,standardsService,$modalInstance)->
    
    $scope.select2 = {}
    $scope.uploaded = []
    $scope.progressPercentage = 0
    
    standardsService.getAllStandards().success((data)->
      $scope.availableStandards = data 
    ).error((data)->
      $log.error(data)
    )

    $scope.upload = (files) ->
      $log.debug($upload)
      if(angular.isDefined(files) and files.length > 0)
        $log.debug("defined")
        _.each(files, (file)->
          $log.debug(file)
          $upload.upload({
            url: 'api/v1/upload'
            file: file
          }).progress((evt)->
            $scope.progressPercentage = parseInt(100.0 * evt.loaded / evt.total)
            $log.debug($scope.progressPercentage)
          ).success((data,status,headers,config)->
            $scope.uploaded.push(data) 
            $scope.progressPercentage = 0
            $scope.picture = undefined
          ).error((data,status,headers,config)->
            $log.error(data)
          )
        )
    $scope.availableEducationLevels = common.educationLevels

    $scope.getStatements = ->
      $scope.select2.statements = []
      standardsService.getStatements($scope.standardSelected.id).success((data)->
        $scope.availableStatements = data.statements
      ).error((data)->
        $log.error("unable to get statements")
      )

    $scope.educationLevelChange = ->
      if(_.isUndefined($scope.select2.educationLevels) || _.isNull($scope.select2.educationLevels) || _.isEmpty($scope.select2.educationLevels)) 
        $scope.getStatements()
        return
      $scope.availableStatements = _.filter($scope.availableStatements, (s) ->
        test = _.filter(s.levels, (l)->
          found = _.find($scope.select2.educationLevels, (e)->
            return l.value is e.value    
          )
          return !_.isUndefined(found)
        )
        return test.length > 0
      ) 
      return

    $scope.showEducationLevels = ->
      return (!_.isUndefined($scope.standardSelected))

    $scope.showStatements = ->
      return !_.isUndefined($scope.availableStatements)

    $scope.ok = ->
      $scope.question.pictures = if angular.isDefined($scope.uploaded) then $scope.uploaded else null 
      $scope.question.statements = []
      $scope.question.statements = _.map($scope.select2.statements, (st)->
        st.statement
      )
      $modalInstance.close($scope.question)

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")
]
