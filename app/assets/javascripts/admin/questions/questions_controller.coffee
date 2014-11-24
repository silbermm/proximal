angular.module("proximal").controller "QuestionsCtrl",[
  "$log"
  "$scope"
  "$state"
  "$stateParams"
  "$modal"
  "toaster"
  "uiGridConstants"
  "prox.common"
  "questionsService"
  ($log,$scope,$state, $stateParams, $modal, toaster, uiGridConstants,common, questionsService) ->
    $scope.page = "Questions Page"
  
    questionsService.getQuestions().success((data)->
      $scope.gridOptions.data = data
    ).error((data)->
      $log.error(data)
    )

    $scope.gridOptions = {
      showFooter: true,
      enableFiltering: true,
      enableRowSelection: true,
      enableSelectAll: true,
      enableSorting: true,
      selectionRowHeaderWidth: 35,
      multiSelect: true,
      columnDefs: [
         { name: 'id' },
         { name: 'text'},
         { name: 'picture'}
      ],
    }

    $scope.addQuestion = ->
      modalInstance = $modal.open({
        templateUrl: "../assets/javascripts/admin/questions/add_question.html"
        controller: "AddQuestionCtrl"
        resolve: {
          "obj" : {}
        }
      })
      modalInstance.result.then((question)->
        #add the question
        questionsService.addQuestion(question).success((data)->
          toaster.pop('success', "Success", "Added the question") 
          $scope.gridOptions.data.push(data); 
        ).error((data,status)->
          toaster.pop('error', "Failure", "Unable to add the question" + data);
        )
        return
      )
    return

]
angular.module("proximal").controller "AddQuestionCtrl",[
  "$log"
  "$scope"
  "$modalInstance"
  "prox.common"
  "obj"
  ($log,$scope,$modalInstance,common,obj)->
      
    $scope.ok = ->
      pic = if angular.isDefined($scope.picture) then $scope.picture.base64 else null 
      $modalInstance.close({
        "text" : $scope.question.text
        "picture" : pic
      })

    $scope.cancel = ->
      $modalInstance.dismiss("cancel")

]
