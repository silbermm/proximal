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
    controller: 'ChildrenCtrl as ctrl',
    templateUrl: '../assets/javascripts/children/templates/children.html',
    data: {title: "Children"}
  }

  $stateProvider.state 'children.view',{
    url: "/{id}",
    controller: 'ViewChildCtrl',
    templateUrl: '../assets/javascripts/children/templates/view_child.html',
    data: {title: "Children"}
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
    url: '/standards/{id}'
    controller: 'StandardsCtrl'
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

  $stateProvider.state 'admin.standards.statements',{
    url: '/statements'
    controller: 'StatementsCtrl'
    templateUrl: '../assets/javascripts/admin/standards/statements/statements.html'
    data: { 
      title: 'Statements', 
      hideStandards: true, 
      anotherData: 'blah', 
      hideAdmin: true
      breadcrumbs: [
        {"path" : "admin", "text" : "Admin" },
        {"path" : "admin.standards", "text": "Standards"}
      ]
    }
  }

  $stateProvider.state 'admin.questions',{
    url: '/questions'
    controller: 'QuestionsCtrl'
    templateUrl: '../assets/javascripts/admin/questions/questions.html'
    data: {
      title: "Questions"
      hideAdmin: true
      breadcrumbs: [
        {"path": "admin", "text": "Admin"}
      ]
    }
  }




  $urlRouterProvider.otherwise "/dashboard"
