angular.module("proximal").config ($stateProvider, $urlRouterProvider) ->

  $stateProvider.state 'home', {
    url: '/home',
    controller: "HomeCtrl as ctrl",
    templateUrl: "../assets/javascripts/home/home.html"
  }

  $stateProvider.state 'dashboard', {
    url: '/dashboard',
    controller: 'DashboardCtrl as ctrl',
    templateUrl: '../assets/javascripts/dashboard/dashboard.html',
    data: {title: "Dashboard"}
  }

  $stateProvider.state 'children', {
    url: '/children',
    controller: 'ChildrenCtrl as childCtrl',
    templateUrl: '../assets/javascripts/children/children.html',
    data: {
      title: "Children"
      hideChildren: false 
    }
  }

  $stateProvider.state 'children.view',{
    url: "/{id}",
    controller: 'ViewChildCtrl'
    controllerAs: 'childctrl'
    templateUrl: '../assets/javascripts/children/view/view_child.html',
    data: {
      title: "Children"
      hideChildren: true
      breadcrumbs: [
        {"path" : "children", "text" : "All Children" },
      ]

    }
  }
  
  $stateProvider.state 'children.view.assesment',{
    url: "/assesment",
    controller: 'AssesmentCtrl'
    controllerAs: 'assesCtrl' 
    templateUrl: '../assets/javascripts/children/view/assesments/assesment.html'
    templateAs: 'assessment'
    data: {
      title: "New Assesment"
      hideChildren: true
      breadcrumbs: [
        {"path" : "children", "text" : "All Children" }
      ]

    }
  }

  $stateProvider.state 'children.view.homework', {
    url: "/homework"
    controller: "HomeworkCtrl"
    controllerAs: 'homework'
    templateUrl: '../assets/javascripts/children/view/homework/homework.html'
    data: {
      title: "Homework"
      hideChildren: true
      breadcrumbs: [
        {"path" : "children", "text" : "All Children" }
      ]
    }
  }

  $stateProvider.state 'library', {
    url: '/library',
    controller: 'LibraryCtrl as ctrl',
    templateUrl: '../assets/javascripts/library/library.html',
    data: { title: 'Library' }
  }

  $stateProvider.state 'settings', {
    url: '/settings',
    controller: 'SettingsCtrl as ctrl',
    templateUrl: '../assets/javascripts/settings/settings.html',
    data: { title: 'Settings' }
  }
  
  $stateProvider.state 'admin', {
    url: '/admin',
    controller: 'AdminCtrl as ctrl',
    templateUrl: '../assets/javascripts/admin/admin.html',
    data: { title: 'Administrative Tasks', hideAdmin: false }
  }
   
  $stateProvider.state 'admin.standards', {
    url: '/standards'
    controller: 'StandardsCtrl as ctrl'
    templateUrl: '../assets/javascripts/admin/standards/standards.html'
    data: { 
      title: 'Standards',
      hideStandards: false,
      hideAdmin: true
      breadcrumbs: [
        {"path" : "admin", "text" : "Admin" },
      ]

    }
  }
  
  $stateProvider.state 'admin.standards.detail', {
    url: '/{id}'
    controller: 'StandardsCtrl as ctrl'
    templateUrl: '../assets/javascripts/admin/standards/view_standard.html'
    data: { 
      title: 'Standards',
      hideStandards: false,
      hideAdmin: true
      breadcrumbs: [
        {"path" : "admin", "text" : "Admin" },
      ]
    }
  }

  $stateProvider.state 'admin.questions',{
    url: '/questions'
    controller: 'QuestionsCtrl as ctrl'
    templateUrl: '../assets/javascripts/admin/questions/questions.html'
    data: {
      title: "Questions"
      hideAdmin: true
      breadcrumbs: [
        {"path": "admin", "text": "Admin"}
      ]
    }
  }

  $stateProvider.state 'admin.questions.edit',{
    url: '/{questionId}'
    controller: 'EditQuestionsCtrl as question'
    templateUrl: '../assets/javascripts/admin/questions/edit/edit_question.html'
    data: {
      title: "Edit"
      hideAdmin: true
      hideQuestions: true
      breadcrumbs: [
        {"path": "admin", "text": "Admin"}
        {"path": "admin.questions", "text": "Questions"}
      ]
    }

  }




  $urlRouterProvider.otherwise "/dashboard"
