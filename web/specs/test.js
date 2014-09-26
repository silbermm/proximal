(function() {
  describe('proximal', function() {
    var controller, log;
    controller = void 0;
    log = void 0;
    beforeEach(function() {
      return module('proximal');
    });
    return describe('HomeController', function() {
      beforeEach(inject(function($controller, $log) {
        controller = $controller;
        log = $log;
        return controller = controller('HomeCtrl', {
          '$log': '$log',
          $log: $log
        });
      }));
      console.log(controller);
      return it('sets the name of the page', function() {
        return expect(controller.page).toBe('Home Page');
      });
    });
  });

}).call(this);

(function() {
  describe('proximal', function() {
    var controller, log;
    controller = void 0;
    log = void 0;
    beforeEach(function() {
      return module('proximal');
    });
    return describe('LoginController', function() {
      beforeEach(inject(function($controller, $log) {
        controller = $controller;
        log = $log;
        return controller = controller('LoginCtrl', {
          '$log': '$log',
          $log: $log
        });
      }));
      console.log(controller);
      return it('sets the name of the page', function() {
        return expect(controller.page).toBe('Login Page');
      });
    });
  });

}).call(this);

(function() {
  describe('footer_directive', function() {
    var element, scope;
    element = void 0;
    scope = void 0;
    beforeEach(module('proximal'));
    beforeEach(inject(function($rootScope, $compile) {
      scope = $rootScope.$new();
      element = '<prox-footer> </prox-footer>';
      element = $compile(element)(scope);
      return scope.$digest();
    }));
    it("should be a footer element", function() {
      return expect(element[0].tagName).toBe("FOOTER");
    });
    return it("should have the copyright variable", function() {
      var isolated;
      isolated = element.isolateScope();
      return expect(isolated.copyright).toBe(new Date().getFullYear());
    });
  });

}).call(this);

(function() {
  describe('header_directive', function() {
    var element, scope;
    element = void 0;
    scope = void 0;
    beforeEach(module('proximal'));
    beforeEach(inject(function($rootScope, $compile) {
      scope = $rootScope.$new();
      element = '<prox-header> </prox-header>';
      element = $compile(element)(scope);
      return scope.$digest();
    }));
    return it("should be a header element", function() {
      return expect(element[0].tagName).toBe("HEADER");
    });
  });

}).call(this);
