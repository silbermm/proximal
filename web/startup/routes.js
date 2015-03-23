'use strict()';

require('../home');
require('../dashboard');
require('../library');
require("../children");
require("../admin");
require("../activities");
require("../resources");

module.exports = function($stateProvider, $urlRouterProvider) {

  $stateProvider.state('home', {
    url: '/home',
    controller: 'HomeController as ctrl',
    templateUrl: 'home/home.html'
  })
  .state('dashboard', {
    url: '/dashboard',
    controller: 'DashboardCtrl as ctrl',
    templateUrl: 'dashboard/dashboard.html',
    data: {title: "Dashboard"}
  })
  .state('library', {
    url: '/library',
    controller: 'LibraryCtrl as ctrl',
    templateUrl: 'library/library.html',
    data: {title: "Library"}
  })
  .state('children', {
    url: '/children',
    controller: 'ChildrenCtrl as childCtrl',
    templateUrl: 'children/children.html',
    data: {
      title: "Children",
      hideChildren: false 
    }
  })
  .state('children.view',{
    url: "/{id}",
    data: {
      title: "Children",
      hideChildren: true,
      breadcrumbs: [
        {"path" : "children", "text" : "All Children" },
      ]
    },
    views: {
      "" : {
        controller: 'ViewChildCtrl',
        controllerAs: 'childctrl',
        templateUrl: 'view/view_child.html' 
      },
      "assessment@children.view" : {
        controller: 'AssessmentCtrl',
        controllerAs: 'assessment', 
        templateUrl: 'assessments/assessment.html',
        data: {
          title: "New Assessment",
          hideChildren: true,
          breadcrumbs: [
            {"path" : "children", "text" : "All Children" }
          ]
        }
      },
      "homework@children.view" : {
        controller: "HomeworkCtrl",
        controllerAs: 'homework',
        templateUrl: 'homework/homework.html',
        data: {
          title: "Homework",
          hideChildren: true,
          hideHomework: false,
          breadcrumbs: [
            {"path" : "children", "text" : "All Children" }
          ]
        }
      },
      "homeworkDetails@children.view" : {
        controller: "HomeworkDetailsCtrl",
        controllerAs: 'details', 
        templateUrl: 'details/homework_details.html',
        data: {
          title: "Homework Details",
          hideChildren: true,
          hideHomework: true,
          breadcrumbs: [
            {"path" : "children", "text" : "All Children" },
            {"path" : "children.view.homework", "text" : "All Homework" }
          ]
        } 
      }
    }
  })
  .state('admin', {
    url: '/admin',
    controller: 'AdminCtrl as ctrl',
    templateUrl: 'admin/admin.html',
    data: { title: 'Administrative Tasks', hideAdmin: false },
  })
  .state('admin.resources', {
    url: '/resources',
    controller: 'ResourcesController as resource',
    templateUrl: 'resources/resources.html',
    data: {
      title: 'Resources',
      hideAdmin: true,
      breadcrumbs: [
        {"path": "admin", "text": "Admin"}
      ]
    }
  })
  .state('admin.standards', {
    url: '/standards',
    controller: 'StandardsCtrl as ctrl',
    templateUrl: 'standards/standards.html',
    data: { 
      title: 'Standards',
      hideStandards: false,
      hideAdmin: true,
      breadcrumbs: [
        {"path" : "admin", "text" : "Admin" },
      ]
    }
  }) 
  .state('admin.standards.detail', {
    url: '/{id}',
    controller: 'StandardsCtrl as ctrl',
    templateUrl: 'standards/view_standard.html',
    data: { 
      title: 'Standards',
      hideStandards: false,
      hideAdmin: true,
      breadcrumbs: [
        {"path" : "admin", "text" : "Admin" },
      ]
    }
  })
  .state('admin.questions',{
    url: '/questions',
    controller: 'QuestionsCtrl as ctrl',
    templateUrl: 'questions/questions.html',
    data: {
      title: "Questions",
      hideAdmin: true,
      breadcrumbs: [
        {"path": "admin", "text": "Admin"}
      ]
    }
  })
  .state('admin.questions.edit',{
    url: '/{questionId}',
    controller: 'EditQuestionsCtrl as question',
    templateUrl: 'edit/edit_question.html',
    data: {
      title: "Edit",
      hideAdmin: true,
      hideQuestions: true,
      breadcrumbs: [
        {"path": "admin", "text": "Admin"},
        {"path": "admin.questions", "text": "Questions"}
      ]
    }
  })
  .state('admin.activities', {
    url: '/activities',
    controller: 'ActivitiesController as activities',
    templateUrl: 'activities/activities.html',
    data: { 
      title: 'Activities',
      hideActivities: false,
      hideAdmin: true,
      breadcrumbs: [
        {"path" : "admin", "text" : "Admin" },
      ]
    }
  }) 
  ;

  $urlRouterProvider.otherwise('/dashboard');
};
