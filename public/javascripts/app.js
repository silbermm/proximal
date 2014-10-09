(function() {
  angular.module('proximal', ["templates-main", "ui.router", "ui.router.state", "ngAnimate", 'ngCookies', 'ui.bootstrap']).run(function($rootScope, $state, $log) {
    return $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
      return $log.debug("state changed!");
    });
  }).controller('AppCtrl', function($scope, $state, $log, $cookieStore) {
    var mobileView;
    $scope.state = $state;
    mobileView = 992;
    $scope.getWidth = function() {
      return window.innerWidth;
    };
    $scope.$watch($scope.getWidth, function(newValue, oldValue) {
      $log.debug("watch has fired");
      if (newValue >= mobileView) {
        if (angular.isDefined($cookieStore.get("toggle"))) {
          if ($cookieStore.get("toggle") === false) {
            return $scope.toggle = false;
          } else {
            return $scope.toggle = true;
          }
        } else {
          return $scope.toggle = true;
        }
      } else {
        $scope.toggle = false;
      }
    });
    $scope.toggleSidebar = function() {
      $log.debug("toggle called");
      $scope.toggle = !$scope.toggle;
      return $cookieStore.put('toggle', $scope.toggle);
    };
    return window.onresize = function() {
      return $scope.$apply();
    };
  });

}).call(this);

(function() {
  angular.module("proximal").config(function($stateProvider, $urlRouterProvider) {
    $stateProvider.state('home', {
      url: '/home',
      controller: "HomeCtrl as ctrl",
      templateUrl: "../home/home.html"
    });
    $stateProvider.state('login', {
      url: '/login',
      controller: 'LoginCtrl as ctrl',
      templateUrl: '../login/login.html'
    });
    $stateProvider.state('dashboard', {
      url: '/dashboard',
      controller: 'DashboardCtrl as ctrl',
      templateUrl: '../dashboard/dashboard.html'
    });
    return $urlRouterProvider.otherwise("/dashboard");
  });

}).call(this);

(function() {
  angular.module("proximal").controller("DashboardCtrl", function($log, $cookieStore, $scope) {
    return $scope.page = "Dashboard Page";
  });

}).call(this);

(function() {
  angular.module("proximal").controller("HomeCtrl", function($log) {
    return this.page = "Home Page";
  });

}).call(this);

(function() {
  angular.module("proximal").controller("LoginCtrl", function($log) {
    return this.page = "Login Page";
  });

}).call(this);

(function() {


}).call(this);

(function() {


}).call(this);

(function() {
  angular.module("proximal").directive("proxFooter", function($log) {
    return {
      restrict: "E",
      scope: {},
      replace: true,
      templateUrl: "../shared/footer/footer.html",
      link: function(scope, element, attr) {
        return scope.copyright = new Date().getFullYear();
      }
    };
  });

}).call(this);

(function() {
  angular.module("proximal").controller("HeaderCtrl", function($log) {});

}).call(this);

(function() {
  angular.module("proximal").directive("proxHeader", function($log) {
    return {
      restrict: "E",
      scope: {
        copyright: '='
      },
      replace: true,
      templateUrl: "../shared/header/header.html",
      controller: "HeaderCtrl",
      link: function(scope, element, attr) {
        return $log.debug(element);
      }
    };
  });

}).call(this);

(function() {
  var proxLoading;

  proxLoading = function() {
    var directive;
    directive = {
      restrict: 'AE',
      template: '<div class="loading"><div class="double-bounce1"></div><div class="double-bounce2"></div></div>'
    };
    return directive;
  };

  angular.module('proximal').directive('proxLoading', proxLoading);

}).call(this);

(function() {
  var proxWidgetBody;

  proxWidgetBody = function() {
    return {
      requires: '^proxWidget',
      scope: {
        loading: '@?'
      },
      transclude: true,
      template: '<div class="widget-body"><prox-loading ng-show="loading"></prox-loading><div ng-hide="loading" class="widget-content" ng-transclude></div></div>',
      restrict: 'E'
    };
  };

}).call(this);

(function() {
  var proxWidgetHeader;

  proxWidgetHeader = function() {
    return {
      requires: '^proxWidget',
      scope: {
        title: '@',
        icon: '@'
      },
      transclude: true,
      template: '<div class="widget-header"> <i class="fa" ng-class="icon"></i> {{title}} <div class="pull-right" ng-transclude></div></div>',
      restrict: 'E'
    };
  };

}).call(this);

(function() {
  var proxWidget;

  proxWidget = function() {
    return {
      transclude: true,
      template: '<div class="widget" ng-transclude></div>',
      restrict: 'EA'
    };
  };

}).call(this);
